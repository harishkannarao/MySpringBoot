package com.harishkannarao.rest.interceptor.response;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.harishkannarao.rest.interceptor.response.RestResponseHeaderHandler.CUSTOM_HEADER_NAME;

@Component
public class HtmlResponseHeaderHandler extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Optional.ofNullable(request.getHeader(CUSTOM_HEADER_NAME))
                .ifPresent(headerValue ->
                    response.addHeader(CUSTOM_HEADER_NAME, headerValue)
                );

    }
}
