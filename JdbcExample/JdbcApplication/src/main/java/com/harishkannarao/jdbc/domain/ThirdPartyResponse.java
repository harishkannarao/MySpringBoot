package com.harishkannarao.jdbc.domain;

public record ThirdPartyResponse(
	String url,
	String responseContent,
	String contentType,
	int status
) {
}
