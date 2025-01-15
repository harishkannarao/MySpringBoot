package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.client.ProxyHttpInterface;
import com.harishkannarao.jdbc.client.SampleHttpInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfiguration {

	@Bean
	public HttpServiceProxyFactory httpServiceProxyFactory(@Qualifier("myRestClient") RestClient restClient) {
		RestClientAdapter adapter = RestClientAdapter.create(restClient);
		return HttpServiceProxyFactory.builderFor(adapter).build();
	}

	@Bean
	public ProxyHttpInterface proxyHttpInterface(HttpServiceProxyFactory factory) {
		return factory.createClient(ProxyHttpInterface.class);
	}

	@Bean
	public SampleHttpInterface sampleHttpInterface(HttpServiceProxyFactory factory) {
		return factory.createClient(SampleHttpInterface.class);
	}
}
