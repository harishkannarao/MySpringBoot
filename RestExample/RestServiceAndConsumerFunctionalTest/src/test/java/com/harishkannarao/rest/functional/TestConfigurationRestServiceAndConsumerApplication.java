package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.rest.client.ThirdPartyPingRestClient;
import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.filter.ErrorSimulationFilter;
import com.harishkannarao.rest.util.PropertiesBasedFeatureToggler;
import com.harishkannarao.rest.util.FeatureToggler;
import com.harishkannarao.rest.util.TestFeatureToggler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.Ordered;

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
    @Qualifier("myTestRestTemplate")
    public TestRestTemplate getMyTestRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .additionalInterceptors(new JsonHeaderInterceptor());
        return new TestRestTemplate(builder);
    }

    @Bean
    @Qualifier("myTestRestTemplateForHtml")
    public TestRestTemplate getMyTestRestTemplateForHtml() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .additionalInterceptors(new HtmlAcceptHeaderInterceptor());
        return new TestRestTemplate(builder);
    }

    @Bean
    @Qualifier("myTestObjectMapper")
    public ObjectMapper getMyTestObjectMapper() {
        return new ObjectMapper();
    }

    @Bean("errorSimulationFilter")
    public FilterRegistrationBean registerErrorSimulationFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new ErrorSimulationFilter());
        filterRegistrationBean.setName(ErrorSimulationFilter.NAME);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        filterRegistrationBean.setUrlPatterns(asList(ErrorSimulationFilter.PATH));
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
