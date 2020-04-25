package com.harishkannarao.jdbc.filter;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class RequestTracingFilter extends OncePerRequestFilter {
    public static final String REQUEST_ID_KEY = "request_id";
    public static final String NAME = "requestIdFilterBean";
    public static final String PATH = "/*";


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        httpServletRequest.setAttribute(REQUEST_ID_KEY, requestId);
        MDC.put(REQUEST_ID_KEY, requestId);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
