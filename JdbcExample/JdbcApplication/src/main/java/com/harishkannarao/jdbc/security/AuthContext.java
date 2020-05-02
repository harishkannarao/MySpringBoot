package com.harishkannarao.jdbc.security;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class AuthContext {
    public static final String CALLER_ATTR_KEY = "caller";

    public Caller verifyRoleAndGetCaller(HttpServletRequest request, String role) {
        Caller caller = getCaller(request);
        if (!caller.getRoles().contains(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Operation not permitted");
        }
        return caller;
    }

    public Caller getCaller(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute(CALLER_ATTR_KEY))
                .map(o -> (Caller) o)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required"));
    }

}
