package com.harishkannarao.jdbc.interceptor;

import com.harishkannarao.jdbc.security.Subject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.harishkannarao.jdbc.security.AuthContext.SUBJECT_ATTR_KEY;

@Component
public class CookieRequestInterceptor implements HandlerInterceptor {

    private static final String SUPER_SECRET_COOKIE_VALUE = "HELLO_COOKIE";
    private static final String COOKIE_NAME = "session_cookie";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getAttribute(SUBJECT_ATTR_KEY) == null && request.getCookies() != null) {
            Optional<String> cookieValue = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue);
            if (cookieValue.isPresent() && SUPER_SECRET_COOKIE_VALUE.equals(cookieValue.get())) {
                Subject subject = new Subject("cookie-id", List.of("cookie-role"));
                request.setAttribute(SUBJECT_ATTR_KEY, subject);
            }
        }
        return true;
    }

}
