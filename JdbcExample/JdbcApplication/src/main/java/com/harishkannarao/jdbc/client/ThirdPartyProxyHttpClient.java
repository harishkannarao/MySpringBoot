package com.harishkannarao.jdbc.client;

import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;

@Component
public class ThirdPartyProxyHttpClient {
	private final RestClient restClient;
	private final URI thirdPartyProxyUrl;

	@Autowired
	public ThirdPartyProxyHttpClient(
		@Qualifier("myRestClient") RestClient restClient,
		@Value("${thirdparty.proxy.url}") URI thirdPartyProxyUrl) {
		this.restClient = restClient;
		this.thirdPartyProxyUrl = thirdPartyProxyUrl;
	}

	public ThirdPartyResponse getResponse() {
		try {
			ResponseEntity<String> exchange = restClient
				.get()
				.uri(thirdPartyProxyUrl)
				.retrieve()
				.toEntity(String.class);
			return new ThirdPartyResponse(
				thirdPartyProxyUrl.toString(),
				exchange.getBody(),
				exchange.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE),
				exchange.getStatusCode().value());
		} catch (HttpStatusCodeException e) {
			return new ThirdPartyResponse(
				thirdPartyProxyUrl.toString(),
				e.getResponseBodyAsString(),
				Optional.ofNullable(e.getResponseHeaders()).map(httpHeaders -> httpHeaders.getFirst(HttpHeaders.CONTENT_TYPE)).orElse(""),
				e.getStatusCode().value());
		}
	}
}
