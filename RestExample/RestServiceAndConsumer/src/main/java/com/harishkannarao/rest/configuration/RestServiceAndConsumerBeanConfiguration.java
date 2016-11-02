package com.harishkannarao.rest.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestServiceAndConsumerBeanConfiguration {

    @Bean
    @Qualifier("myRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
