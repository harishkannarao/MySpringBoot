package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = {
                RestServiceAndConsumerApplication.class,
                TestConfigurationFunctionalTestsWithoutMocks.class
        },
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=8180",
                "management.port=8181",
                "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
        })
public abstract class BaseIntegrationWithTestConfiguration {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;
}

