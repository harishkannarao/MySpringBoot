package com.harishkannarao.jdbc;

import com.fasterxml.jackson.databind.JsonNode;
import com.harishkannarao.jdbc.client.SampleHttpInterface;
import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Objects;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SampleHttpInterfaceIT extends BaseIntegrationJdbc {
	@Value("${thirdparty.customer.url}")
	private String thirdPartyCustomerRestUrl;
	@Autowired
	private SampleHttpInterface sampleHttpInterface;

	@Test
	public void getCustomerDetails() {
		String customerId = UUID.randomUUID().toString();
		wireMock.register(
			get(urlPathEqualTo("/customer/" + customerId))
				.willReturn(
					aResponse()
						.withBody("""
							{"name": "test-customer"}
							""")
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withStatus(200)
				)
		);

		ResponseEntity<JsonNode> response = sampleHttpInterface.getCustomerDetails(
			new DefaultUriBuilderFactory(thirdPartyCustomerRestUrl), customerId);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		JsonNode body = Objects.requireNonNull(response.getBody());
		assertThat(body.get("name").asText()).isEqualTo("test-customer");
	}

	@Test
	public void getCustomerOrderDetails() {
		String customerId = UUID.randomUUID().toString();
		String orderId = UUID.randomUUID().toString();
		wireMock.register(
			get(urlPathEqualTo("/customer/" + customerId + "/orders/" + orderId))
				.willReturn(
					aResponse()
						.withBody("""
							{"orderDescription": "test-order"}
							""")
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withStatus(200)
				)
		);

		ResponseEntity<JsonNode> response = sampleHttpInterface.getOrderDetails(
			new DefaultUriBuilderFactory(thirdPartyCustomerRestUrl), customerId, orderId);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		JsonNode body = Objects.requireNonNull(response.getBody());
		assertThat(body.get("orderDescription").asText()).isEqualTo("test-order");
	}

}
