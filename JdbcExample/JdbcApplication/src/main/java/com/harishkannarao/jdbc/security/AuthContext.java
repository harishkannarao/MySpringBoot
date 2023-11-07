package com.harishkannarao.jdbc.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class AuthContext {
    public static final String SUBJECT_ATTR_KEY = "subject";

    public Subject getSubject(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute(SUBJECT_ATTR_KEY))
                .map(o -> (Subject) o)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required"));
    }

}
