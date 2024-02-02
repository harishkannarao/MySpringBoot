package com.harishkannarao.jdbc.client.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestClientAccessLoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientAccessLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        long startTime = System.currentTimeMillis();
        int statusCode = 0;
        String method = httpRequest.getMethod().name();
        String url = httpRequest.getURI().toString();
        ClientHttpResponse response;
        try {
            response = clientHttpRequestExecution.execute(httpRequest, bytes);
            statusCode = response.getStatusCode().value();
        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;
            LOGGER.info("REST_CLIENT_ACCESS_LOG {} {} {} {}", timeTaken, statusCode, method, url);
        }
        return response;
    }
}
