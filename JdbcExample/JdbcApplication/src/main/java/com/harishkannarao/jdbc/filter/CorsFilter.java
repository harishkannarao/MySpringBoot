package com.harishkannarao.jdbc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CorsFilter extends OncePerRequestFilter {
    private final Logger LOG = LoggerFactory.getLogger(CorsFilter.class);
    private final List<String> corsOriginList;

    public CorsFilter(String corsOrigins) {
        this.corsOriginList = Arrays.asList(corsOrigins.split(","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String originUpperCase = httpServletRequest.getHeader("Origin");
        String originLowerCase = httpServletRequest.getHeader("origin");

        LOG.info("Values of origin headers: " + originUpperCase + " " + originLowerCase);

        if (corsOriginList.contains(originUpperCase) || corsOriginList.contains(originLowerCase)) {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Location");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
