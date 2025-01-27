package com.harishkannarao.jdbc.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
	url = "/customer/{customerId}",
	contentType = MediaType.APPLICATION_JSON_VALUE,
	accept = MediaType.APPLICATION_JSON_VALUE)
public interface SampleHttpInterface extends WithRetries {

	@GetExchange
	ResponseEntity<JsonNode> getCustomerDetails(
		@PathVariable("customerId") String customerId);

	@GetExchange(url = "/orders/{orderId}")
	ResponseEntity<JsonNode> getOrderDetails(
		@PathVariable("customerId") String customerId,
		@PathVariable("orderId") String OrderId);

	@PostExchange(url = "/orders")
	ResponseEntity<Void> createOrder(
		@RequestHeader("request-id") String requestId,
		@PathVariable("customerId") String customerId,
		@RequestBody JsonNode body);
}
