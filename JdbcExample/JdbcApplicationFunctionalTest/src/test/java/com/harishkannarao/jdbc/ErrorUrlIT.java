package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class ErrorUrlIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${nonExistentEndpointUrl}")
	private String nonExistentEndpointUrl;

	@Test
	public void test404_forNonExistentEndpoint() {
		final var exception = catchThrowableOfType(
			() -> restTemplate.exchange(nonExistentEndpointUrl, HttpMethod.GET, null, Void.class),
			HttpClientErrorException.class);
		assertThat(exception.getStatusCode().value()).isEqualTo(404);
	}

}
