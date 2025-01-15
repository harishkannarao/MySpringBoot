package com.harishkannarao.jdbc.client;

import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ThirdPartyProxyHttpClientTest {

	private final ProxyHttpInterface mockInterface = mock();
	private final String thirdPartyUrl = "http://www.example.com";
	private final ThirdPartyProxyHttpClient underTest = new ThirdPartyProxyHttpClient(
		mockInterface, thirdPartyUrl);

	@Test
	public void test_successful_response() {
		when(mockInterface.makeGet(any()))
			.thenReturn(ResponseEntity.ok("response-body"));

		ThirdPartyResponse result = underTest.getResponse();

		assertThat(result.url()).isEqualTo(thirdPartyUrl);
		assertThat(result.status()).isEqualTo(200);
		assertThat(result.responseContent()).isEqualTo("response-body");

		verify(mockInterface).makeGet(assertArg(uriBuilderFactory ->
			assertThat(uriBuilderFactory.uriString("").toUriString())
				.isEqualTo(thirdPartyUrl)));
	}

	@Test
	public void test_error_response() {
		when(mockInterface.makeGet(any()))
			.thenReturn(ResponseEntity.badRequest().body("response-body"));

		ThirdPartyResponse result = underTest.getResponse();

		assertThat(result.url()).isEqualTo(thirdPartyUrl);
		assertThat(result.status()).isEqualTo(400);
		assertThat(result.responseContent()).isEqualTo("response-body");

		verify(mockInterface).makeGet(assertArg(uriBuilderFactory ->
			assertThat(uriBuilderFactory.uriString("").toUriString())
				.isEqualTo(thirdPartyUrl)));
	}
}