package com.harishkannarao.jdbc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.harishkannarao.jdbc.client.SampleHttpInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

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
	public void getCustomerDetails_throwsException_afterRetry_onHttpServerError() {
		String customerId = UUID.randomUUID().toString();
		wireMock.register(
			get(urlPathEqualTo("/customer/" + customerId))
				.willReturn(
					aResponse()
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withStatus(503)
				)
		);

		HttpServerErrorException.ServiceUnavailable result = catchThrowableOfType(
			() -> sampleHttpInterface.getCustomerDetails(customerId),
			HttpServerErrorException.ServiceUnavailable.class);
		assertThat(result).isNotNull();

		List<LoggedRequest> loggedRequests = wireMock.find(getRequestedFor(urlPathEqualTo("/customer/" + customerId)));
		assertThat(loggedRequests)
			.hasSize(4);
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
	public void getOptionalOrderDetails_returns_details() {
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

		Optional<JsonNode> response = sampleHttpInterface
			.getOptionalOrderDetails(headers, customerId, orderId);

		assertThat(response.isPresent()).isTrue();
		assertThat(response.get().get("orderDescription").asText()).isEqualTo("test-order");

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
	public void getOptionalOrderDetails_returns_empty_for_404() {
		String customerId = UUID.randomUUID().toString();
		String orderId = UUID.randomUUID().toString();
		wireMock.register(
			get(urlPathEqualTo("/customer/" + customerId + "/orders/" + orderId))
				.willReturn(
					aResponse()
						.withStatus(404)
				)
		);

		HttpHeaders headers = new HttpHeaders();
		headers.add("request-id", UUID.randomUUID().toString());
		headers.add("correlation-id", UUID.randomUUID().toString());

		Optional<JsonNode> response = sampleHttpInterface
			.getOptionalOrderDetails(headers, customerId, orderId);

		assertThat(response).isEmpty();

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
	public void getOptionalOrderDetails_throws_exception_after_retry() {
		String customerId = UUID.randomUUID().toString();
		String orderId = UUID.randomUUID().toString();
		wireMock.register(
			get(urlPathEqualTo("/customer/" + customerId + "/orders/" + orderId))
				.willReturn(
					aResponse()
						.withStatus(502)
				)
		);

		HttpHeaders headers = new HttpHeaders();
		headers.add("request-id", UUID.randomUUID().toString());
		headers.add("correlation-id", UUID.randomUUID().toString());

		HttpServerErrorException result = catchThrowableOfType(
			HttpServerErrorException.class,
			() -> sampleHttpInterface
				.getOptionalOrderDetails(headers, customerId, orderId));

		assertThat(result.getStatusCode().value()).isEqualTo(502);

		List<LoggedRequest> requests = wireMock.find(getRequestedFor(
			urlPathEqualTo("/customer/" + customerId + "/orders/" + orderId)));

		assertThat(requests)
			.hasSize(4);
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
