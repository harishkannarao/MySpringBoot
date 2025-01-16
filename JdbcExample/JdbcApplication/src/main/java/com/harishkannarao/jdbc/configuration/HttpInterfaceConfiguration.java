package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.client.ProxyHttpInterface;
import com.harishkannarao.jdbc.client.SampleHttpInterface;
import com.harishkannarao.jdbc.client.factory.HttpInterfaceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpInterfaceConfiguration {

	@Bean
	public ProxyHttpInterface proxyHttpInterface(HttpInterfaceFactory factory) {
		return factory.createClient(ProxyHttpInterface.class);
	}

	@Bean
	public SampleHttpInterface sampleHttpInterface(
		HttpInterfaceFactory factory,
		@Value("${thirdparty.customer.url}") String thirdPartyCustomerRestUrl) {
		return factory.createClient(SampleHttpInterface.class, thirdPartyCustomerRestUrl);
	}
}
