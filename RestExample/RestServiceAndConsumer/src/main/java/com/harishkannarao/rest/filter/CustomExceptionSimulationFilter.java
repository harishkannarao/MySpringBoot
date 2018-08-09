package com.harishkannarao.rest.filter;

import com.harishkannarao.rest.exception.MyCustomRuntimeException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomExceptionSimulationFilter extends OncePerRequestFilter {
    public static final String NAME = "customExceptionSimulationFilterBean";
    public static final String PATH = "/simulate-custom-error";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
        throw new MyCustomRuntimeException("ERR123", "Unique error");
    }
}
