package com.harishkannarao.rest.filter;

import com.harishkannarao.rest.exception.MyCustomRuntimeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;


public class CustomExceptionSimulationFilter extends OncePerRequestFilter {
    public static final String NAME = "customExceptionSimulationFilterBean";
    public static final String PATH = "/simulate-custom-error";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
        throw new MyCustomRuntimeException("ERR123", "Unique error");
    }
}
