package com.harishkannarao.rest.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

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
