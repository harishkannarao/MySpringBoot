package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

import static com.harishkannarao.rest.filter.ResponseHeaderFilter.CUSTOM_HEADER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestExceptionSimulationControllerIT extends BaseIntegration {

	@org.springframework.beans.factory.annotation.Value("${generateRestRuntimeExceptionUrl}")
	public String generateRestRuntimeExceptionUrl;
	@org.springframework.beans.factory.annotation.Value("${generateRestCheckedExceptionUrl}")
	public String generateRestCheckedExceptionUrl;
	@org.springframework.beans.factory.annotation.Value("${generateRestCustomRuntimeExceptionUrl}")
	public String generateRestCustomRuntimeExceptionUrl;
	@org.springframework.beans.factory.annotation.Value("${generateRestCustomCheckedExceptionUrl}")
	public String generateRestCustomCheckedExceptionUrl;

	@Test
	public void shouldGet500StatusWithMessageForCheckedException() throws Exception {
		EntityExchangeResult<String> response = testRestTemplate.get()
			.uri(generateRestCheckedExceptionUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(500, response.getStatus().value());
		ErrorResponse errorResponse = objectMapper.readValue(response.getResponseBody(), ErrorResponse.class);
		assertEquals("My Sample Checked Exception", errorResponse.getMessage());
	}

	@Test
	public void shouldGet400StatusWithMessageForRuntimeException() throws Exception {
		EntityExchangeResult<String> response = testRestTemplate.get()
			.uri(generateRestRuntimeExceptionUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(400, response.getStatus().value());
		ErrorResponse errorResponse = objectMapper.readValue(response.getResponseBody(), ErrorResponse.class);
		assertEquals("My Sample Runtime Exception", errorResponse.getMessage());
	}

	@Test
	public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomRuntimeException() throws Exception {
		EntityExchangeResult<String> response = testRestTemplate.get()
			.uri(generateRestCustomRuntimeExceptionUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(403, response.getStatus().value());
		ErrorResponse errorResponse = objectMapper.readValue(response.getResponseBody(), ErrorResponse.class);
		assertEquals("CustomRuntime:My Custom Runtime Exception", errorResponse.getMessage());
		assertEquals("CustomRuntime", errorResponse.getCode());
		assertEquals("My Custom Runtime Exception", errorResponse.getDescription());
	}

	@Test
	public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomCheckedException() throws Exception {
		EntityExchangeResult<String> response = testRestTemplate.get()
			.uri(generateRestCustomCheckedExceptionUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(403, response.getStatus().value());
		ErrorResponse errorResponse = objectMapper.readValue(response.getResponseBody(), ErrorResponse.class);
		assertEquals("CustomChecked:My Custom Checked Exception", errorResponse.getMessage());
		assertEquals("CustomChecked", errorResponse.getCode());
		assertEquals("My Custom Checked Exception", errorResponse.getDescription());
	}

	@Test
	public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
		String customHeaderValue = "someValue";
		EntityExchangeResult<String> response = testRestTemplate.method(HttpMethod.GET)
			.uri(generateRestCustomCheckedExceptionUrl)
			.header(CUSTOM_HEADER_NAME, customHeaderValue)
			.exchange()
			.returnResult(String.class);
		assertEquals(403, response.getStatus().value());
		assertEquals(customHeaderValue, response.getResponseHeaders().getFirst(CUSTOM_HEADER_NAME));
	}

}
