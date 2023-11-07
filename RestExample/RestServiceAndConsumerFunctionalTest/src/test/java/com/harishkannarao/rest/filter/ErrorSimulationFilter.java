package com.harishkannarao.rest.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;


public class ErrorSimulationFilter extends OncePerRequestFilter {
    public static final String NAME = "errorSimulationFilterBean";
    public static final String PATH = "/simulate-filter-error";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
        throw new RuntimeException("artificial exception simulation outside controller");
    }
}
