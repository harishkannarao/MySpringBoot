package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.client.ThirdPartyProxyHttpClient;
import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyProxyController {

	private final ThirdPartyProxyHttpClient thirdPartyProxyHttpClient;

	@Autowired
	public ThirdPartyProxyController(ThirdPartyProxyHttpClient thirdPartyProxyHttpClient) {
		this.thirdPartyProxyHttpClient = thirdPartyProxyHttpClient;
	}

	@GetMapping("/third-party-response")
	public ResponseEntity<ThirdPartyResponse> getResponse() {
		return ResponseEntity.ok()
			.body(thirdPartyProxyHttpClient.getResponse());
	}
}
