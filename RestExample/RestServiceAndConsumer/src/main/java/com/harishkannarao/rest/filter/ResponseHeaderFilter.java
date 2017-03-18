package com.harishkannarao.rest.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ResponseHeaderFilter extends OncePerRequestFilter {
    public static final String CUSTOM_HEADER_NAME = "MyCustomHeader";
    public static final String NAME = "responseHeaderFilterBean";
    public static final String PATH = "/*";


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(httpServletRequest.getHeader(CUSTOM_HEADER_NAME))
                .ifPresent(headerValue ->
                        httpServletResponse.addHeader(CUSTOM_HEADER_NAME, headerValue)
                );
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
