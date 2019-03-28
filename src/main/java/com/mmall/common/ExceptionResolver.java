package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * <p>
 * @Author LeifChen
 * @Date 2019-03-26
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("{} Exception", request.getRequestURI(), ex);
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        mv.addObject("status", ResponseCodeEnum.ERROR.getCode());
        mv.addObject("msg", "接口异常，详情请查看服务端日志");
        mv.addObject("data", ex.toString());
        return mv;
    }
}
