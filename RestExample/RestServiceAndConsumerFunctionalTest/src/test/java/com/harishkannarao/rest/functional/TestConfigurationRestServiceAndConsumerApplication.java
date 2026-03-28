package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.client.ThirdPartyPingRestClient;
import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.filter.ErrorSimulationFilter;
import com.harishkannarao.rest.util.PropertiesBasedFeatureToggler;
import com.harishkannarao.rest.util.FeatureToggler;
import com.harishkannarao.rest.util.TestFeatureToggler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@PropertySources({
        @PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfigurationRestServiceAndConsumerApplication {
    @Bean
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    @Primary
    public ThirdPartyRestQuoteClient overrideThirdPartyRestQuoteClientWithMockForTesting() {
        return mock(ThirdPartyRestQuoteClient.class);
    }

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

		@Bean
		@Qualifier("myRestTestClientForHtml")
		public RestTestClient getRestTestClientForHtml() {
			return RestTestClient.bindToServer()
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_HTML_VALUE)
				.build();
		}

    @Bean
    @Qualifier("myTestObjectMapper")
    public ObjectMapper getMyTestObjectMapper() {
        return new ObjectMapper();
    }

    @Bean("errorSimulationFilter")
    public FilterRegistrationBean<ErrorSimulationFilter> registerErrorSimulationFilter() {
        FilterRegistrationBean<ErrorSimulationFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ErrorSimulationFilter());
        filterRegistrationBean.setName(ErrorSimulationFilter.NAME);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(List.of(ErrorSimulationFilter.PATH));
        return filterRegistrationBean;
    }

    @Bean
    @Primary
    public FeatureToggler testFeatureToggler(PropertiesBasedFeatureToggler propertiesBasedFeatureToggler) {
        return new TestFeatureToggler(propertiesBasedFeatureToggler);
    }

    @Bean
    public WebDriverFactory createWebDriverFactorySingleton() {
        return new WebDriverFactory();
    }

}
