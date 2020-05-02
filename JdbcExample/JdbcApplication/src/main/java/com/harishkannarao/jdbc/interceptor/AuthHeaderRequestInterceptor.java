package com.harishkannarao.jdbc.interceptor;

import com.harishkannarao.jdbc.security.Caller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static com.harishkannarao.jdbc.security.AuthContext.CALLER_ATTR_KEY;

@Component
public class AuthHeaderRequestInterceptor extends HandlerInterceptorAdapter {

    private static final String SUPER_SECRET_HEADER_VALUE = "HELLO_HEADER";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getAttribute(CALLER_ATTR_KEY) == null) {
            Optional<String> authHeader = extractBearerToken(request);
            if (authHeader.isPresent() && SUPER_SECRET_HEADER_VALUE.equals(authHeader.get())) {
                Caller caller = new Caller("header-id", List.of("header-role"));
                request.setAttribute(CALLER_ATTR_KEY, caller);
            }
        }
        return true;
    }

    public Optional<String> extractBearerToken(HttpServletRequest request) {
        Optional<String> authHeader = extractAuthHeader(request);
        if (authHeader.isPresent()) {
            String value = authHeader.get();
            if (value.startsWith(BEARER_PREFIX)) {
                return Optional.of(value.substring(BEARER_PREFIX.length()));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    private Optional<String> extractAuthHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER));
    }

}
