package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.security.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("ConstantConditions")
public class ExampleAuthRestControllerIT extends BaseIntegrationJdbc {
	@Value("${exampleAuthHeaderEndpointUrl}")
	private String exampleAuthHeaderEndpointUrl;
	@Value("${exampleAuthCookieEndpointUrl}")
	private String exampleAuthCookieEndpointUrl;

	@Test
	public void auth_header_should_return_valid_caller() {
		ResponseEntity<Subject> response = restClient
			.get()
			.uri(exampleAuthHeaderEndpointUrl)
			.header("Authorization", "Bearer HELLO_HEADER")
			.retrieve()
			.toEntity(Subject.class);
		Subject entity = response.getBody();

		assertThat(response.getStatusCode().value(), equalTo(200));
		assertThat(entity.getId(), equalTo("header-id"));
		assertThat(entity.getRoles(), containsInAnyOrder("header-role"));
	}

	@Test
	public void auth_header_should_return_401_for_missing_header() {
		try {
			restClient
				.get()
				.uri(exampleAuthHeaderEndpointUrl)
				.retrieve()
				.toBodilessEntity();
			fail("Should have thrown exception");
		} catch (HttpClientErrorException result) {
			assertThat(result.getStatusCode().value(), equalTo(401));
			assertThat(result.getMessage(), containsString("Authentication required"));
		}
	}

	@Test
	public void auth_header_should_return_401_for_invalid_header() {
		try {
			restClient
				.get()
				.uri(exampleAuthHeaderEndpointUrl)
				.header("Authorization", "Bearer invalid-value")
				.retrieve()
				.toBodilessEntity();
			fail("Should have thrown exception");
		} catch (HttpClientErrorException result) {
			assertThat(result.getStatusCode().value(), equalTo(401));
			assertThat(result.getMessage(), containsString("Authentication required"));
		}
	}

	@Test
	public void auth_header_should_return_403_for_incorrect_role() {
		try {
			restClient
				.get()
				.uri(exampleAuthHeaderEndpointUrl)
				.header("Cookie", "session_cookie=HELLO_COOKIE;")
				.retrieve()
				.toBodilessEntity();
			fail("Should have thrown exception");
		} catch (HttpClientErrorException result) {
			assertThat(result.getStatusCode().value(), equalTo(403));
			assertThat(result.getMessage(), containsString("Operation not permitted"));
		}
	}

	@Test
	public void auth_cookie_should_return_valid_caller() {
		ResponseEntity<Subject> response = restClient
			.get()
			.uri(exampleAuthCookieEndpointUrl)
			.header("Cookie", "session_cookie=HELLO_COOKIE;")
			.retrieve()
			.toEntity(Subject.class);
		Subject entity = response.getBody();

		assertThat(response.getStatusCode().value(), equalTo(200));
		assertThat(entity.getId(), equalTo("cookie-id"));
		assertThat(entity.getRoles(), containsInAnyOrder("cookie-role"));
	}

	@Test
	public void auth_cookie_should_return_401_for_missing_header() {
		try {
			restClient
				.get()
				.uri(exampleAuthCookieEndpointUrl)
				.retrieve()
				.toBodilessEntity();
			fail("Should have thrown exception");
		} catch (HttpClientErrorException result) {
			assertThat(result.getStatusCode().value(), equalTo(401));
			assertThat(result.getMessage(), containsString("Authentication required"));
		}
	}

	@Test
	public void auth_cookie_should_return_401_for_invalid_header() {
		try {
			restClient
				.get()
				.uri(exampleAuthCookieEndpointUrl)
				.header("Cookie", "session_cookie=invalid_cookie;")
				.retrieve()
				.toBodilessEntity();
			fail("Should have thrown exception");
		} catch (HttpClientErrorException result) {
			assertThat(result.getStatusCode().value(), equalTo(401));
			assertThat(result.getMessage(), containsString("Authentication required"));
		}
	}

	@Test
	public void auth_cookie_should_return_403_for_incorrect_role() {
		try {
			restClient
				.get()
				.uri(exampleAuthCookieEndpointUrl)
				.header("Authorization", "Bearer HELLO_HEADER")
				.retrieve()
				.toBodilessEntity();
			fail("Should have thrown exception");
		} catch (HttpClientErrorException result) {
			assertThat(result.getStatusCode().value(), equalTo(403));
			assertThat(result.getMessage(), containsString("Operation not permitted"));
		}
	}
}
