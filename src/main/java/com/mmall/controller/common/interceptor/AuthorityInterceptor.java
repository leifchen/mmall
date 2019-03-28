package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.JsonResult;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtils;
import com.mmall.util.JsonUtils;
import com.mmall.util.RedisShardedPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * 权限拦截器
 * <p>
 * @Author LeifChen
 * @Date 2019-03-26
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    private static final String USER_MANAGE_CONTROLLER = "UserManageController";
    private static final String PRODUCT_MANAGE_CONTROLLER = "ProductManageController";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        StringBuilder requestParamBuffer = new StringBuilder();
        Map paramMap = request.getParameterMap();
        for (Object o : paramMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;
            // request参数的map里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        // 放行管理员的登录请求
        if (StringUtils.equals(className, USER_MANAGE_CONTROLLER)
                && StringUtils.equals(methodName, "login")) {
            log.info("权限拦截器拦截到请求,className:{},methodName:{}", className, methodName);
            return true;
        }

        log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}", className, methodName, requestParamBuffer.toString());

        User user = null;
        String loginToken = CookieUtils.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisShardedPoolUtils.get(loginToken);
            user = JsonUtils.string2Obj(userJsonStr, User.class);
        }

        if (user == null || (user.getRole() != Const.Role.ROLE_ADMIN)) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();
            // 特殊处理上传富文本的请求
            if (user == null) {
                if (StringUtils.equals(className, PRODUCT_MANAGE_CONTROLLER)
                        && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "请登录管理员");
                    out.print(JsonUtils.obj2String(resultMap));
                } else {
                    out.print(JsonUtils.obj2String(JsonResult.error("拦截器拦截，用户未登录")));
                }
            } else {
                if (StringUtils.equals(className, PRODUCT_MANAGE_CONTROLLER)
                        && StringUtils.equals(methodName, "richtextImgUpload")) {
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    out.print(JsonUtils.obj2String(resultMap));
                } else {
                    out.print(JsonUtils.obj2String(JsonResult.error("拦截器拦截，用户无权限操作")));
                }
            }
            out.flush();
            out.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("afterCompletion");
    }
}
