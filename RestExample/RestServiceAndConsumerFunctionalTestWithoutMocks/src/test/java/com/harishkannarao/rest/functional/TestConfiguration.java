package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import com.harishkannarao.stub.RestServiceThirdPartyStubApplication;
import com.harishkannarao.rest.client.ThirdPartyPingRestClient;
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
@Import({RestServiceAndConsumerApplication.class, RestServiceThirdPartyStubApplication.class})
@PropertySources({
        @PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfiguration {

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
    @Qualifier("myTestRestTemplate")
    public RestTemplate getMyTestRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(jsonHeaderInterceptor);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
