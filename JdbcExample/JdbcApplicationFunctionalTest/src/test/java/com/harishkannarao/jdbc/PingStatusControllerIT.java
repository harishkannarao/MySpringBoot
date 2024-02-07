package com.harishkannarao.jdbc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.harishkannarao.jdbc.client.interceptor.RestClientAccessLoggingInterceptor;
import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class PingStatusControllerIT extends BaseIntegrationJdbc {
    @Value("${pingStatusEndpointUrl}")
    String pingStatusEndpointUrl;
    @Value("${thirdparty.ping.url}")
    String thirdPartyPingRestUrl;

    private final LogbackTestAppender logbackTestAppender = new LogbackTestAppender(RestClientAccessLoggingInterceptor.class.getName(), Level.INFO);

    @BeforeEach
    public void setUp() {
        logbackTestAppender.startLogsCapture();
    }

    @AfterEach
    public void tearDown() {
        logbackTestAppender.stopLogsCapture();
    }


    @Test
    public void getPingStatus_shouldReturnStatusOfThirdPartUrl() {
        wireMock.register(
                get(urlEqualTo("/ping"))
                        .willReturn(
                                aResponse()
                                        .withStatus(204)
                        )
        );

        ThirdPartyStatus status = restClient
					.get()
					.uri(pingStatusEndpointUrl)
					.retrieve()
					.body(ThirdPartyStatus.class);

        assertThat(status.status()).isEqualTo(204);
        assertThat(status.url()).isEqualTo(thirdPartyPingRestUrl);
    }

    @Test
    public void getPingStatus_should_capture_rest_template_metrics() {
        wireMock.register(
                get(urlEqualTo("/ping"))
                        .willReturn(
                                aResponse()
                                        .withStatus(204)
                        )
        );

        ThirdPartyStatus status = restClient
					.get()
					.uri(pingStatusEndpointUrl)
					.retrieve()
					.body(ThirdPartyStatus.class);

        assertThat(status.status()).isEqualTo(204);
        assertThat(status.url()).isEqualTo(thirdPartyPingRestUrl);

        assertThat(logbackTestAppender.getLogs())
                        .extracting(ILoggingEvent::getFormattedMessage)
                                .anySatisfy(s -> assertThat(s).matches("REST_CLIENT_ACCESS_LOG .* 204 GET http://localhost:9010/ping"));
    }
}
