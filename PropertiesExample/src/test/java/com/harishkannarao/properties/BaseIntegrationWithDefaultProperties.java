package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = {
                PropertiesApplication.class,
                TestConfigurationPropertiesApplication.class
        },
        webEnvironment = DEFINED_PORT
)
@ActiveProfiles(value = {"default", "properties-test"})
public abstract class BaseIntegrationWithDefaultProperties {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate testRestTemplate;
}
