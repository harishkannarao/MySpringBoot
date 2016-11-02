package com.harishkannarao.rest.config;

import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import com.harishkannarao.rest.client.ThirdPartyPingRestClient;
import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.client.JsonHeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import({RestServiceAndConsumerApplication.class})
@PropertySources({
        @PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfiguration {

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

    @Autowired
    private JsonHeaderInterceptor jsonHeaderInterceptor;

    @Bean
    @Qualifier("myTestRestTemplate")
    public RestTemplate getMyTestRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(jsonHeaderInterceptor);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
