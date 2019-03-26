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
@RequestMapping("/user/session")
public class UserSessionController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public JsonResult<User> login(String username, String password, HttpSession session) {
        int i = 1 / 0;

        JsonResult<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public JsonResult<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return JsonResult.success();
    }

    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.GET)
    public JsonResult<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return JsonResult.success(user);
        } else {
            return JsonResult.error("用户未登录，无法获取当前用户信息");
        }
    }
}
