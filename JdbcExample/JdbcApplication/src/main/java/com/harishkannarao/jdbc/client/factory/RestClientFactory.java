package com.harishkannarao.jdbc.client.factory;

import com.harishkannarao.jdbc.client.interceptor.RestClientAccessLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class RestClientFactory {
	private final long connectTimeoutMs;
	private final long readTimeoutMs;

	@Autowired
	public RestClientFactory(
		@Value("${app.rest-client.connect-timeout-ms}") final long connectTimeoutMs,
		@Value("${app.rest-client.read-timeout-ms}") final long readTimeoutMs) {
		this.connectTimeoutMs = connectTimeoutMs;
		this.readTimeoutMs = readTimeoutMs;
	}

	public RestClient create() {
		return createRestClient(null);
	}

	public RestClient create(String baseUrl) {
		return createRestClient(baseUrl);
	}

	private RestClient createRestClient(String baseUrl) {
		RestClientAccessLoggingInterceptor restClientAccessLoggingInterceptor = new RestClientAccessLoggingInterceptor();
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
		simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));
		BufferingClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

		RestClient.Builder builder = RestClient.builder()
			.requestFactory(clientHttpRequestFactory)
			.requestInterceptor(restClientAccessLoggingInterceptor);

		if (baseUrl != null) {
			builder.baseUrl(baseUrl);
		}
		return builder
			.build();
	}
}
