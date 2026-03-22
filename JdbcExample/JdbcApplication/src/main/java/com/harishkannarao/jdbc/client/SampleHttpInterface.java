package com.harishkannarao.jdbc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import tools.jackson.databind.JsonNode;

import java.util.Optional;

@HttpExchange(
	url = "/customer/{customerId}",
	contentType = MediaType.APPLICATION_JSON_VALUE,
	accept = MediaType.APPLICATION_JSON_VALUE)
public interface SampleHttpInterface extends WithRetries {

	Logger log = LoggerFactory.getLogger(SampleHttpInterface.class);

	@GetExchange
	ResponseEntity<JsonNode> getCustomerDetails(
		@PathVariable("customerId") String customerId);

	@GetExchange(url = "/orders/{orderId}")
	ResponseEntity<JsonNode> getOrderDetails(
		@PathVariable("customerId") String customerId,
		@PathVariable("orderId") String orderId,
		@RequestHeader("request-id") String requestId,
		@RequestHeader("correlation-id") String correlationId);

	default Optional<JsonNode> getOptionalOrderDetails(
		String customerId,
		String orderId,
		String requestId,
		String correlationId) {
		try {
			ResponseEntity<JsonNode> orderDetails = getOrderDetails(customerId, orderId, requestId, correlationId);
			return Optional.ofNullable(orderDetails.getBody());
		} catch (HttpClientErrorException.NotFound notFound) {
			log.info("Received {}", notFound.getStatusCode().value());
			return Optional.empty();
		}
	}

	@PostExchange(url = "/orders")
	ResponseEntity<Void> createOrder(
		@RequestHeader("request-id") String requestId,
		@PathVariable("customerId") String customerId,
		@RequestBody JsonNode body);
}
