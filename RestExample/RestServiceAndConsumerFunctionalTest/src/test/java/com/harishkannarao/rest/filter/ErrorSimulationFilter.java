package com.harishkannarao.rest.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorSimulationFilter extends OncePerRequestFilter {
    public static final String NAME = "errorSimulationFilterBean";
    public static final String PATH = "/simulate-filter-error";


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        throw new RuntimeException("artificial exception simulation outside controller");
    }
}
