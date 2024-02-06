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
import java.util.Objects;

@Component
public class RequestResponseBodyLoggingInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseBodyLoggingInterceptor.class);

	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		long startTime = System.currentTimeMillis();
		String method = httpRequest.getMethod().name();
		String url = httpRequest.getURI().toString();
		String headers = httpRequest.getHeaders().toString();
		String reqBody = new String(body, StandardCharsets.UTF_8);
		ClientHttpResponse response = null;
		try {
			LOGGER.info("TEST_REST_CLIENT_REQUEST {} {} {} {}", method, url, headers, reqBody);
			response = clientHttpRequestExecution.execute(httpRequest, body);
		} finally {
			long timeTaken = System.currentTimeMillis() - startTime;
			String resBody = null;
			String resHeaders = null;
			int status = 0;
			if (Objects.nonNull(response)) {
				resBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
				status = response.getStatusCode().value();
				resHeaders = response.getHeaders().toString();
			}
			LOGGER.info("TEST_REST_CLIENT_RESPONSE {} {} {} {} {} {}", timeTaken, method, url, status, resHeaders, resBody);
		}
		return response;
	}
}
