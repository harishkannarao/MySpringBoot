package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.client.interceptor.RestTemplateAccessLoggingInterceptor;
import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("ConstantConditions")
public class PingStatusControllerIT extends BaseIntegrationJdbc {
    @Value("${pingStatusEndpointUrl}")
    String pingStatusEndpointUrl;
    @Value("${thirdparty.ping.url}")
    String thirdPartyPingRestUrl;

    @Rule
    public final LogbackTestAppenderRule testAppenderRule = new LogbackTestAppenderRule(RestTemplateAccessLoggingInterceptor.class.getName());

    @Test
    public void getPingStatus_shouldReturnStatusOfThirdPartUrl() {
        wireMock.register(
                get(urlEqualTo("/ping"))
                        .willReturn(
                                aResponse()
                                        .withStatus(204)
                        )
        );

        ThirdPartyStatus status = restTemplate.getForObject(pingStatusEndpointUrl, ThirdPartyStatus.class);

        assertThat(status.getStatus(), equalTo(204));
        assertThat(status.getUrl(), equalTo(thirdPartyPingRestUrl));
    }

    @Test
    public void getPingStatus_should_capture_rest_template_metrics() throws Exception {
        wireMock.register(
                get(urlEqualTo("/ping"))
                        .willReturn(
                                aResponse()
                                        .withStatus(204)
                        )
        );

        ThirdPartyStatus status = restTemplate.getForObject(pingStatusEndpointUrl, ThirdPartyStatus.class);

        assertThat(status.getStatus(), equalTo(204));
        assertThat(status.getUrl(), equalTo(thirdPartyPingRestUrl));

        testAppenderRule.assertLogEntry("INFO  REST_CLIENT_ACCESS_LOG");
        testAppenderRule.assertLogEntry("204 GET http://localhost:9010/ping");
    }
}
