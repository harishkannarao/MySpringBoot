package com.harishkannarao.jdbc;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.harishkannarao.jdbc.client.interceptor.RestClientAccessLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
@PropertySources({
	@PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfigurationJdbcApplication {

	private static final Logger log = LoggerFactory.getLogger(TestConfigurationJdbcApplication.class);

	@Bean
	public WireMockServer createWireMockServer() {
		WireMockServer wireMockServer = new WireMockServer(0);
		wireMockServer.start();
		return wireMockServer;
	}

	@Bean
	public WireMock createWireMockClient(WireMockServer wireMockServer) {
		return new WireMock(wireMockServer.port());
	}

	@Bean
	public DisposableBean stopWireMockServer(WireMockServer wireMockServer) {
		return () -> {
			log.info("Stopping wiremock.port as {}", wireMockServer.port());
			wireMockServer.stop();
		};
	}

	@Bean
	public DynamicPropertyRegistrar registerWireMockPort(WireMockServer wireMockServer) {
		return (DynamicPropertyRegistry registry) -> {
			log.info("Registering wiremock.port property as {}", wireMockServer.port());
			registry.add("wiremock.port", wireMockServer::port);
		};
	}

	@Bean
	public DisposableBean stopPostgres() {
		return () -> {
			log.info("Stopping postgres");
			if (Postgres.CONTAINER.isRunning()) {
				Postgres.CONTAINER.stop();
			}
		};
	}

	@Bean
	@Qualifier("myTestRestClient")
	public RestClient getMyTestRestClient(
		JsonHeaderInterceptor jsonHeaderInterceptor,
		RequestResponseBodyLoggingInterceptor requestResponseBodyLoggingInterceptor) {
		SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(3000));
		simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(15000));
		BufferingClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory);

		return RestClient.builder()
			.requestFactory(clientHttpRequestFactory)
			.requestInterceptor(jsonHeaderInterceptor)
			.requestInterceptor(requestResponseBodyLoggingInterceptor)
			.requestInterceptor(new RestClientAccessLoggingInterceptor())
			.build();
	}

	@Bean
	public WebDriverFactory createWebDriverFactorySingleton() {
		return new WebDriverFactory();
	}
}
