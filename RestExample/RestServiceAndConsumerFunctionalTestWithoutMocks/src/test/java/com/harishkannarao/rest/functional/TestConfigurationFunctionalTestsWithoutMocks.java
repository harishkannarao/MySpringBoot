package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.client.ThirdPartyPingRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@PropertySources({
	@PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfigurationFunctionalTestsWithoutMocks {

	@Autowired
	private JsonHeaderInterceptor jsonHeaderInterceptor;

	@Bean
	@Qualifier("myThirdPartyPingRestClientImpl")
	@Primary
	public ThirdPartyPingRestClient overrideThirdPartyPingRestClientWithMockForTesting() {
		ThirdPartyPingRestClient mockedThirdPartyPingRestClient = mock(ThirdPartyPingRestClient.class);
		when(mockedThirdPartyPingRestClient.getPingStatus()).thenReturn("healthy");
		return mockedThirdPartyPingRestClient;
	}

	@Bean
	@Qualifier("myRestTestClient")
	public RestTestClient getRestTestClient() {
		return RestTestClient.bindToServer()
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}
