package com.mmall.controller.common;

import com.mmall.common.JsonResult;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtils;
import com.mmall.util.JsonUtils;
import com.mmall.util.RedisShardedPoolUtils;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * @param httpServletRequest
     * @return
     */
    public JsonResult checkLogin(HttpServletRequest httpServletRequest) {
        String loginToken = CookieUtils.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return JsonResult.error("用户未登录");
        }
        String userJsonStr = RedisShardedPoolUtils.get(loginToken);
        User user = JsonUtils.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return JsonResult.error(ResponseCodeEnum.NEED_LOGIN.getCode(), ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return JsonResult.success(user);
    }
}
