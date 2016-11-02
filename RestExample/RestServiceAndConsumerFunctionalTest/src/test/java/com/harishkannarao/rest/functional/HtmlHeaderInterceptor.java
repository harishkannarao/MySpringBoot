package com.harishkannarao.rest.functional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HtmlHeaderInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        return clientHttpRequestExecution.execute(httpRequest, body);
    }
}
