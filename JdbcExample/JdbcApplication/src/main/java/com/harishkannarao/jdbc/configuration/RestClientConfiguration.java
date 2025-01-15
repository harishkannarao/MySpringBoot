package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.client.interceptor.RestClientAccessLoggingInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {

	@Bean
	@Qualifier("myRestClient")
	public RestClient getRestClient(
		@Value("${app.rest-client.connect-timeout-ms}") final long connectTimeoutMs,
		@Value("${app.rest-client.read-timeout-ms}") final long readTimeoutMs) {
		RestClientAccessLoggingInterceptor restClientAccessLoggingInterceptor = new RestClientAccessLoggingInterceptor();
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
		simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));
		BufferingClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

		return RestClient.builder()
			.requestFactory(clientHttpRequestFactory)
			.requestInterceptor(restClientAccessLoggingInterceptor)
			.build();
	}
}
