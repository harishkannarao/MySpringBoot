package com.harishkannarao.rest.interceptor.response;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ResponseHeaderHandler implements ResponseBodyAdvice {

    public static final String CUSTOM_HEADER_NAME = "MyCustomHeader";
    @Override
    public boolean supports(MethodParameter methodParameter, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class converterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpHeaders requestHeaders = serverHttpRequest.getHeaders();
        if(requestHeaders.containsKey(CUSTOM_HEADER_NAME)) {
            serverHttpResponse.getHeaders().add(CUSTOM_HEADER_NAME, requestHeaders.getFirst(CUSTOM_HEADER_NAME));
        }
        return body;
    }
}
