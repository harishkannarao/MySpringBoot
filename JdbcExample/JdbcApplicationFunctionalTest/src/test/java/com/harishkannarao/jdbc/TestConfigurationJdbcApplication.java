package com.harishkannarao.jdbc;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.harishkannarao.jdbc.client.interceptor.RestClientAccessLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
@PropertySources({
	@PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfigurationJdbcApplication {

	@Autowired
	private JsonHeaderInterceptor jsonHeaderInterceptor;

	@Autowired
	private RequestResponseBodyLoggingInterceptor requestResponseBodyLoggingInterceptor;

	@Value("${wiremock.port}")
	int wireMockPort;

	@Bean(destroyMethod = "stop")
	public WireMockServer createWireMockServer() {
		WireMockServer wireMockServer = new WireMockServer(options().port(wireMockPort));
		wireMockServer.start();
		return wireMockServer;
	}

	@Bean
	public WireMock createWireMockClient(WireMockServer wireMockServer) {
		return new WireMock(wireMockServer.port());
	}

	@Bean
	@Qualifier("myTestRestClient")
	public RestClient getMyTestRestClient() {
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(3000));
		simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(15000));
		BufferingClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

		return RestClient.builder()
			.requestFactory(clientHttpRequestFactory)
			.requestInterceptor(jsonHeaderInterceptor)
			.requestInterceptor(new RestClientAccessLoggingInterceptor())
			.build();
	}

	@Bean
	public WebDriverFactory createWebDriverFactorySingleton() {
		return new WebDriverFactory();
	}
}
