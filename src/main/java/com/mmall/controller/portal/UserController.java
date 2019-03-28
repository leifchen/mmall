package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.controller.common.UserCheck;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.CookieUtils;
import com.mmall.util.JsonUtils;
import com.mmall.util.RedisShardedPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 门户-用户Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-02-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserCheck userCheck;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public JsonResult<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        JsonResult<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            CookieUtils.writeLoginToken(httpServletResponse, session.getId());
            RedisShardedPoolUtils.setEx(session.getId(), JsonUtils.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.POST)
    public JsonResult<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String loginToken = CookieUtils.readLoginToken(httpServletRequest);
        CookieUtils.delLoginToken(httpServletRequest, httpServletResponse);
        RedisShardedPoolUtils.del(loginToken);
        return JsonResult.success();
    }

    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    public JsonResult<String> register(User user) {
        return userService.register(user);
    }

    @RequestMapping(value = "/check_valid.do", method = RequestMethod.POST)
    public JsonResult<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.POST)
    public JsonResult<User> getUserInfo(HttpServletRequest httpServletRequest) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return JsonResult.success(user);
        } else {
            return JsonResult.error("用户未登录，无法获取当前用户信息");
        }
    }

    @RequestMapping(value = "/forget_get_question.do", method = RequestMethod.POST)
    public JsonResult<String> forgetGetQuestion(String username) {
        return userService.selectQuestion(username);
    }

    @RequestMapping(value = "/forget_check_answer.do", method = RequestMethod.POST)
    public JsonResult<String> forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "/forget_reset_password.do", method = RequestMethod.POST)
    public JsonResult<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "/reset_password.do", method = RequestMethod.POST)
    public JsonResult<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            return userService.resetPassword(passwordOld, passwordNew, user);
        } else {
            return JsonResult.error("用户未登录");
        }
    }

    @RequestMapping(value = "/update_information.do", method = RequestMethod.POST)
    public JsonResult<User> updateInformation(HttpServletRequest httpServletRequest, User user) {
        JsonResult response = userCheck.checkLogin(httpServletRequest);
        if (response.isSuccess()) {
            User currentUser = (User) response.getData();
            user.setId(currentUser.getId());
            user.setUsername(currentUser.getUsername());
            JsonResult<User> result = userService.updateInformation(user);
            if (result.isSuccess()) {
                result.getData().setUsername(currentUser.getUsername());
                String loginToken = CookieUtils.readLoginToken(httpServletRequest);
                RedisShardedPoolUtils.setEx(loginToken, JsonUtils.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
            return result;
        } else {
            return JsonResult.error("用户未登录");
        }
    }
}
