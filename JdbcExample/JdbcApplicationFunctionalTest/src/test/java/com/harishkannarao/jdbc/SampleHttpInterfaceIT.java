package com.harishkannarao.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.harishkannarao.jdbc.client.SampleHttpInterface;
import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SampleHttpInterfaceIT extends BaseIntegrationJdbc {
	@Autowired
	private ObjectMapper objectMapper;
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

		ResponseEntity<JsonNode> response = sampleHttpInterface.getCustomerDetails(customerId);

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

		ResponseEntity<JsonNode> response = sampleHttpInterface
			.getOrderDetails(customerId, orderId);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		JsonNode body = Objects.requireNonNull(response.getBody());
		assertThat(body.get("orderDescription").asText()).isEqualTo("test-order");
	}

	@Test
	public void createCustomerOrder() throws Exception {
		String customerId = UUID.randomUUID().toString();
		wireMock.register(
			post(urlPathEqualTo("/customer/" + customerId + "/orders"))
				.willReturn(
					aResponse()
						.withStatus(200)
				)
		);

		String requestId = UUID.randomUUID().toString();
		JsonNode requestBody = objectMapper.readTree("""
			{"orderId" : "some-order-id"}
			""");

		ResponseEntity<Void> response = sampleHttpInterface
			.createOrder(requestId, customerId, requestBody);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		List<LoggedRequest> requests = wireMock.find(postRequestedFor(
			urlPathEqualTo("/customer/" + customerId + "/orders")));

		assertThat(requests)
			.anySatisfy(request -> {
				assertThat(request.getHeader("request-id"))
					.as("verifying request id")
					.isEqualTo(requestId);
				JsonNode sentBody = objectMapper.readTree(request.getBodyAsString());
				assertThat(sentBody)
					.usingRecursiveComparison()
					.ignoringCollectionOrder()
					.isEqualTo(requestBody);
			})
			.hasSize(1);
	}

}
