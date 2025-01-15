package com.harishkannarao.jdbc.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.util.UriBuilderFactory;

public interface ProxyHttpInterface {

	@GetExchange
	ResponseEntity<String> makeGet(UriBuilderFactory uriBuilderFactory);
}
