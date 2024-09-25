package com.harishkannarao.jdbc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.harishkannarao.jdbc.client.interceptor.RestClientAccessLoggingInterceptor;
import com.harishkannarao.jdbc.domain.ThirdPartyResponse;
import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class ThirdPartyProxyControllerIT extends BaseIntegrationJdbc {
	@Value("${proxyEndpointUrl}")
	String proxyEndpointUrl;
	@Value("${thirdparty.proxy.url}")
	String thirdPartyProxyRestUrl;

	@Test
	public void getPingStatus_shouldReturnStatusOfThirdPartUrl() {
		wireMock.register(
			get(urlEqualTo("/proxy"))
				.willReturn(
					aResponse()
						.withBody("Hello!!!")
						.withHeader(HttpHeaders.CONTENT_TYPE, "plain/text")
						.withStatus(200)
				)
		);

		ThirdPartyResponse response = restClient
			.get()
			.uri(proxyEndpointUrl)
			.retrieve()
			.body(ThirdPartyResponse.class);

		assertThat(response.status()).isEqualTo(200);
		assertThat(response.url()).isEqualTo(thirdPartyProxyRestUrl);
		assertThat(response.contentType()).isEqualTo("plain/text");
		assertThat(response.responseContent()).isEqualTo("Hello!!!");
	}

	@Test
	public void getPingStatus_shouldReturnStatusOfThirdPartUrl_onErrorStatus() {
		wireMock.register(
			get(urlEqualTo("/proxy"))
				.willReturn(
					aResponse()
						.withBody("Exception!!!")
						.withHeader(HttpHeaders.CONTENT_TYPE, "plain/text")
						.withStatus(500)
				)
		);

		ThirdPartyResponse response = restClient
			.get()
			.uri(proxyEndpointUrl)
			.retrieve()
			.body(ThirdPartyResponse.class);

		assertThat(response.status()).isEqualTo(500);
		assertThat(response.url()).isEqualTo(thirdPartyProxyRestUrl);
		assertThat(response.contentType()).isEqualTo("plain/text");
		assertThat(response.responseContent()).isEqualTo("Exception!!!");
	}

}
