package com.harishkannarao.jdbc.client;

import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Optional;

@Component
public class ThirdPartyProxyHttpClient {
	private final ProxyHttpInterface proxyHttpInterface;
	private final String thirdPartyProxyUrl;
	private final DefaultUriBuilderFactory uriBuilderFactory;

	@Autowired
	public ThirdPartyProxyHttpClient(
		ProxyHttpInterface proxyHttpInterface,
		@Value("${thirdparty.proxy.url}") String thirdPartyProxyUrl) {
		this.proxyHttpInterface = proxyHttpInterface;
		this.thirdPartyProxyUrl = thirdPartyProxyUrl;
		uriBuilderFactory = new DefaultUriBuilderFactory(thirdPartyProxyUrl);
	}

	public ThirdPartyResponse getResponse() {
		try {
			ResponseEntity<String> exchange = proxyHttpInterface
				.makeGet(uriBuilderFactory);
			return new ThirdPartyResponse(
				thirdPartyProxyUrl,
				exchange.getBody(),
				exchange.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE),
				exchange.getStatusCode().value());
		} catch (HttpStatusCodeException e) {
			return new ThirdPartyResponse(
				thirdPartyProxyUrl,
				e.getResponseBodyAsString(),
				Optional.ofNullable(e.getResponseHeaders()).map(httpHeaders -> httpHeaders.getFirst(HttpHeaders.CONTENT_TYPE)).orElse(""),
				e.getStatusCode().value());
		}
	}
}
