package com.harishkannarao.jdbc.client.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class HttpInterfaceFactory {

	private final RestClientFactory restClientFactory;

	@Autowired
	public HttpInterfaceFactory(RestClientFactory restClientFactory) {
		this.restClientFactory = restClientFactory;
	}

	public <S> S createClient(Class<S> serviceType) {
		return HttpServiceProxyFactory
			.builderFor(RestClientAdapter.create(restClientFactory.create()))
			.build()
			.createClient(serviceType);
	}

	public <S> S createClient(Class<S> serviceType, String baseUrl) {
		return HttpServiceProxyFactory
			.builderFor(RestClientAdapter.create(restClientFactory.create(baseUrl)))
			.build()
			.createClient(serviceType);
	}
}
