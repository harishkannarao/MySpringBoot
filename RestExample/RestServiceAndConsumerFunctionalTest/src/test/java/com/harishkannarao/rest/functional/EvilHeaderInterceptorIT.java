package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.web.util.UriComponentsBuilder;

import static com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor.EVIL_HEADER_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvilHeaderInterceptorIT extends BaseIntegration {

	@org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}")
	public String greetingEndpointUrl;
	@org.springframework.beans.factory.annotation.Value("${helloPageEndpointUrl}")
	public String helloPageEndpointUrl;


	@Test
	public void shouldGet400StatusWithDescriptionForHttpClients() throws Exception {
		String requestUrl = UriComponentsBuilder.fromUriString(greetingEndpointUrl).queryParam("name", "Harish").toUriString();
		EntityExchangeResult<String> response = restTestClient.get()
			.uri(requestUrl)
			.header(EVIL_HEADER_NAME, "Something")
			.exchange()
			.returnResult(String.class);
		assertEquals(400, response.getStatus().value());
		ErrorResponse errorResponse = objectMapper.readValue(response.getResponseBody(), ErrorResponse.class);
		assertEquals("You are an evil request::/rest-service/greeting/get?name=Harish", errorResponse.getDescription());
	}


	@Test
	public void shouldGet400StatusWithDescriptionForHtmlClients() {
		EntityExchangeResult<String> response = restTestClientForHtml.get()
			.uri(helloPageEndpointUrl)
			.header(EVIL_HEADER_NAME, "Something")
			.exchange()
			.returnResult(String.class);
		assertEquals(400, response.getStatus().value());
		assertThat(response.getResponseBody(), containsString("You are an evil request::/rest-service/hello"));
		assertThat(response.getResponseBody(), containsString("Something went wrong in controller:"));
	}

}
