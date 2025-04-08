package com.harishkannarao.jdbc.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

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
		@RequestHeader("headers") HttpHeaders headers,
		@PathVariable("customerId") String customerId,
		@PathVariable("orderId") String orderId);

	default Optional<JsonNode> getOptionalOrderDetails(
		HttpHeaders headers,
		String customerId,
		String orderId) {
		try {
			ResponseEntity<JsonNode> orderDetails = getOrderDetails(headers, customerId, orderId);
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
