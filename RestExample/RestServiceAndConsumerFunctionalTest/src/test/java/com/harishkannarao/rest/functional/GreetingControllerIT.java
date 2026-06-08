package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.Greeting;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.harishkannarao.rest.filter.ResponseHeaderFilter.CUSTOM_HEADER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GreetingControllerIT extends BaseIntegration {

	@org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}")
	public String greetingEndpointUrl;
	@org.springframework.beans.factory.annotation.Value("${greetingWithNameEndpointUrl}")
	public String greetingWithNameEndpointUrl;

	@Test
	public void greeting_shouldReturnDefaultGreeting_givenNameIsNotInQueryParam() {
		Greeting result = restTestClient.get()
			.uri(greetingEndpointUrl)
			.exchange()
			.returnResult(Greeting.class)
			.getResponseBody();
		assertNotNull(result.getId());
		assertEquals("Hello, World!", result.getContent());
	}

	@Test
	public void greeting_shouldReturnGreetingWithName_givenNameInQueryParam() {
		Greeting result = Objects.requireNonNull(
			restTestClient
				.get()
				.uri(UriComponentsBuilder.fromUriString(greetingEndpointUrl)
					.queryParam("name", "Harish")
					.build()
					.toUri())
				.exchange()
				.returnResult(Greeting.class)
				.getResponseBody()
		);

		assertThat(result.getId()).isGreaterThan(0);
		assertEquals("Hello, Harish!", result.getContent());
	}

	@Test
	public void greeting_shouldReturnGreetingWithName_givenPostWithNameAsJson() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("name", "Harish");
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		EntityExchangeResult<String> response = restTestClient.post()
			.uri(greetingEndpointUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(body)
			.exchangeSuccessfully()
			.returnResult(String.class);
		assertEquals(HttpStatus.OK, response.getStatus());
		JsonNode jsonResponse = objectMapper.readTree(response.getResponseBody());
		assertEquals("Hello, Harish!", jsonResponse.get("greeting").asString());
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
		String customHeaderValue = "someValue";
		EntityExchangeResult<Greeting> response = restTestClient.get()
			.uri(UriComponentsBuilder.fromUriString(greetingWithNameEndpointUrl)
				.queryParam("name", "Harish")
				.build()
				.toUri())
			.header(CUSTOM_HEADER_NAME, customHeaderValue)
			.exchangeSuccessfully()
			.returnResult(Greeting.class);
		Greeting result = response.getResponseBody();

		assertEquals(200, response.getStatus().value());
		assertEquals("Hello, Harish!", result.getContent());
		assertEquals(customHeaderValue, response.getResponseHeaders().getFirst(CUSTOM_HEADER_NAME));

	}

}
