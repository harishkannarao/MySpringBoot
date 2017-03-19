package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {
                RestServiceAndConsumerApplication.class,
                TestConfigurationRestServiceAndConsumerApplication.class
        },
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=${RANDOM_PORT_3:8180}",
                "management.port=${RANDOM_PORT_4:8181}",
                "logging.level.root=DEBUG",
                "thirdparty.ping.url=http://localhost:${server.port}/thirdparty/ping",
                "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
        })
public abstract class BaseIntegration {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected TestRestTemplate testRestTemplate;
    @Autowired
    @Qualifier("myTestRestTemplateForHtml")
    protected TestRestTemplate testRestTemplateForHtml;
    @Autowired
    @Qualifier("myTestObjectMapper")
    protected ObjectMapper objectMapper;
    @Autowired
    protected WebDriver webDriver;
    @Autowired
    protected ConfigurableEnvironment configurableEnvironment;
}
