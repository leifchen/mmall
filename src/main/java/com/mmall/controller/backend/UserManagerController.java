package com.mmall.controller.backend;

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
 * 后台管理-用户Controller
 * <p>
 * @Author LeifChen
 * @Date 2019-02-27
 */
@RestController
@RequestMapping("/admin/user")
public class UserManagerController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public JsonResult<User> login(String username, String password, HttpSession session) {
        JsonResult<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return JsonResult.error("不是管理员，无法登录");
            }
        }
        return response;
    }
}
