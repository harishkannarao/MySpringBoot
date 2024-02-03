package com.harishkannarao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class RequestResponseBodyLoggingInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseBodyLoggingInterceptor.class);

	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		ClientHttpResponse response = null;
		try {
			String reqBody = new String(body, StandardCharsets.UTF_8);
			LOGGER.info("REST_CLIENT_REQUEST_BODY_LOG {}", reqBody);
			response = clientHttpRequestExecution.execute(httpRequest, body);
		} finally {
			String resBody = Optional.ofNullable(response).map(clientHttpResponse -> {
                try {
                    return new String(clientHttpResponse.getBody().readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).orElse(null);
			LOGGER.info("REST_CLIENT_RESPONSE_BODY_LOG {}", resBody);
		}
		return response;
	}
}
