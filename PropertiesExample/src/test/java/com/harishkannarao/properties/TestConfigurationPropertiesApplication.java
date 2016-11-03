package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class TestConfigurationPropertiesApplication {
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
