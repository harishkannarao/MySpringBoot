package com.harishkannarao.jdbc.interceptor;

import com.harishkannarao.jdbc.security.AuthContext;
import com.harishkannarao.jdbc.security.Subject;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SubjectRoleInterceptor implements HandlerInterceptor {

    private final AuthContext authContext;

    @Autowired
    public SubjectRoleInterceptor(AuthContext authContext) {
        this.authContext = authContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Optional<RolesAllowed> rolesAllowed = Optional.ofNullable(method.getMethodAnnotation(RolesAllowed.class));
            rolesAllowed.ifPresent(allowed -> {
                Objects.requireNonNull(allowed.value());
                List<String> roles = Arrays.stream(allowed.value()).collect(Collectors.toList());
                Subject subject = authContext.getSubject(request);
                List<String> matchedRoles = subject.getRoles().stream()
                        .filter(roles::contains)
                        .collect(Collectors.toList());
                if (matchedRoles.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Operation not permitted");
                }
            });
        }
        return true;
    }
}