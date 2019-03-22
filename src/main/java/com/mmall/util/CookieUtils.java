package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-03-20
 */
@Slf4j
public class CookieUtils {

    private static final String COOKIE_DOMAIN = "mall.com";
    private static final String COOKIE_NAME = "mall_login_token";

    /**
     * 写cookie
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        // 设置在根目录
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // 如果不设置，则cookie不写入硬盘，而写在内存。只在当前页面有效。
        cookie.setMaxAge(60 * 60 * 24 * 365);
        log.info("Write cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     * 读cookie
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("Read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("Return cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookie
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    // 设置成0，表示删除此cookie
                    cookie.setMaxAge(0);
                    log.info("Delete cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
