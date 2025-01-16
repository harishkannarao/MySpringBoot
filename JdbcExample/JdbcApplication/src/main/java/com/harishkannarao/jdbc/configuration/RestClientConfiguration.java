package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.client.factory.RestClientFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

	@Bean
	@Qualifier("myRestClient")
	public RestClient getRestClient(RestClientFactory restClientFactory) {
		return restClientFactory.create();
	}
}
