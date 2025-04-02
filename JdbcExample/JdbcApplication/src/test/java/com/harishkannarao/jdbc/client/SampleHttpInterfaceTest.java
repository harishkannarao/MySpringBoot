package com.harishkannarao.jdbc.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.harishkannarao.jdbc.client.factory.HttpInterfaceFactory;
import com.harishkannarao.jdbc.client.factory.RestClientFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class SampleHttpInterfaceTest {
	private final WireMockServer wireMockServer = new WireMockServer(0);
	private final RestClientFactory restClientFactory = new RestClientFactory(
		200, 200);
	private final HttpInterfaceFactory httpInterfaceFactory = new HttpInterfaceFactory(
		restClientFactory);
	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
	private SampleHttpInterface sampleHttpInterface;
	private WireMock wireMock;

	@BeforeEach
	public void setUp() {
		wireMockServer.start();
		wireMockServer.resetAll();
		wireMock = new WireMock(wireMockServer.port());
		sampleHttpInterface = httpInterfaceFactory.createClient(SampleHttpInterface.class, wireMockServer.baseUrl());
	}

	@AfterEach
	public void tearDown() {
		wireMockServer.stop();
	}

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
	public void getCustomerDetails_throwsException_withoutRetry_onInternalServerError() {
		String customerId = UUID.randomUUID().toString();
		wireMock.register(
			get(urlPathEqualTo("/customer/" + customerId))
				.willReturn(
					aResponse()
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withStatus(500)
				)
		);

		HttpServerErrorException.InternalServerError result = catchThrowableOfType(
			() -> sampleHttpInterface.getCustomerDetails(customerId),
			HttpServerErrorException.InternalServerError.class);
		assertThat(result).isNotNull();

		List<LoggedRequest> loggedRequests = wireMock.find(getRequestedFor(urlPathEqualTo("/customer/" + customerId)));
		assertThat(loggedRequests)
			.hasSize(1);
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

		HttpHeaders headers = new HttpHeaders();
		headers.add("request-id", UUID.randomUUID().toString());
		headers.add("correlation-id", UUID.randomUUID().toString());

		ResponseEntity<JsonNode> response = sampleHttpInterface
			.getOrderDetails(headers, customerId, orderId);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		JsonNode body = Objects.requireNonNull(response.getBody());
		assertThat(body.get("orderDescription").asText()).isEqualTo("test-order");

		List<LoggedRequest> requests = wireMock.find(getRequestedFor(
			urlPathEqualTo("/customer/" + customerId + "/orders/" + orderId)));

		assertThat(requests)
			.hasSize(1)
			.anySatisfy(request -> {
				assertThat(request.getHeader("request-id"))
					.as("verifying request id")
					.isEqualTo(headers.getFirst("request-id"));
				assertThat(request.getHeader("correlation-id"))
					.as("verifying correlation id")
					.isEqualTo(headers.getFirst("correlation-id"));
			});
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
