package com.mmall.controller.common;

import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.CookieUtils;
import com.mmall.util.JsonUtils;
import com.mmall.util.RedisPoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验用户
 * <p>
 * @Author LeifChen
 * @Date 2019-03-21
 */
@Component
public class UserCheck {

    @Autowired
    private UserService userService;

    /**
     * 校验管理员角色是否登录
     * @param httpServletRequest
     * @return
     */
    public JsonResult checkAdminRoleLogin(HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtils.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return JsonResult.error("用户未登录");
        }
        String userJsonStr = RedisPoolUtils.get(loginToken);
        User user = JsonUtils.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        // 校验是否管理员
        if (userService.checkAdminRole(user).isSuccess()) {
            return JsonResult.success();
        } else {
            return JsonResult.error("无权限操作，需要管理员权限");
        }
    }

    /**
     * @param httpServletRequest
     * @return
     */
    public JsonResult checkLogin(HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtils.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return JsonResult.error("用户未登录");
        }
        String userJsonStr = RedisPoolUtils.get(loginToken);
        User user = JsonUtils.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return JsonResult.success(user);
    }
}
