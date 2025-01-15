package com.harishkannarao.jdbc.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.util.UriBuilderFactory;

@HttpExchange(
	url = "/customer/{customerId}",
	contentType = MediaType.APPLICATION_JSON_VALUE,
	accept = MediaType.APPLICATION_JSON_VALUE)
public interface SampleHttpInterface {

	@GetExchange
	ResponseEntity<JsonNode> getCustomerDetails(
		UriBuilderFactory uriBuilderFactory,
		@PathVariable("customerId") String customerId);

	@GetExchange(url = "/orders/{orderId}")
	ResponseEntity<JsonNode> getOrderDetails(
		UriBuilderFactory uriBuilderFactory,
		@PathVariable("customerId") String customerId,
		@PathVariable("orderId") String OrderId);
}
