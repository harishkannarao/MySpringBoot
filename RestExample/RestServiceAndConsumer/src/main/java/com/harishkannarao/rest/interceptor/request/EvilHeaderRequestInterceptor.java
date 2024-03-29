package com.harishkannarao.rest.interceptor.request;

import com.harishkannarao.rest.exception.EvilHeaderException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.apache.logging.log4j.util.Strings.isEmpty;


@Component
public class EvilHeaderRequestInterceptor implements HandlerInterceptor {
    public static final String EVIL_HEADER_NAME = "MyEvilHeader";

    private final String evilHeaderMessage;


    @Autowired
    public EvilHeaderRequestInterceptor(@org.springframework.beans.factory.annotation.Value("${evil.header.message}")String evilHeaderMessage) {
        this.evilHeaderMessage = evilHeaderMessage;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String evilHeaderValue = request.getHeader(EVIL_HEADER_NAME);
        if(!isEmpty(evilHeaderValue)) {
            String requestedUri = request.getRequestURI();
            String requestedQueryParam = request.getQueryString();
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append(evilHeaderMessage);
            messageBuilder.append("::");
            messageBuilder.append(requestedUri);
            if(!isEmpty(requestedQueryParam)) {
                messageBuilder.append("?");
                messageBuilder.append(requestedQueryParam);
            }
            throw new EvilHeaderException(messageBuilder.toString());
        }
        return true;
    }

}
