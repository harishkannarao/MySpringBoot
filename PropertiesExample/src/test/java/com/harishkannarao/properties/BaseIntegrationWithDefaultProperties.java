package com.harishkannarao.properties;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {
                PropertiesApplication.class,
                TestConfigurationPropertiesApplication.class
        },
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=${RANDOM_PORT_1:8280}",
                "management.port=${RANDOM_PORT_2:8281}",
                "properties.endpoint.url=http://localhost:${server.port}"
        })
public abstract class BaseIntegrationWithDefaultProperties {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate testRestTemplate;
}
