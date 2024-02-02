package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiSwaggerIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${swaggerEndpointUrl}")
	private String swaggerEndpointUrl;
	@Autowired
	@Value("${openApiEndpointUrl}")
	private String openApiEndpointUrl;

	@Test
	public void testSwaggerUrl() {
		ResponseEntity<Void> response = restClient
			.get()
			.uri(swaggerEndpointUrl)
			.retrieve()
			.toBodilessEntity();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	public void testOpenApiUrl() {
		ResponseEntity<Void> response = restClient
			.get()
			.uri(openApiEndpointUrl)
			.retrieve()
			.toBodilessEntity();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

}
