package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public JsonResult<User> login(String username, String password, HttpSession session) {
        JsonResult<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.POST)
    public JsonResult<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
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
    public JsonResult<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return JsonResult.success(user);
        }
        return JsonResult.error("用户未登陆，无法获取当前用户信息");
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
    public JsonResult<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return JsonResult.error("用户未登录");
        }
        return userService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "/update_information.do", method = RequestMethod.POST)
    public JsonResult<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return JsonResult.error("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        JsonResult<User> response = userService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }
}
