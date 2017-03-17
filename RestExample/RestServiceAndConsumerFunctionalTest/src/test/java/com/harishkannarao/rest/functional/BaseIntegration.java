package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
                "thirdparty.ping.url=http://localhost:${server.port}/thirdparty/ping",
                "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
        })
public abstract class BaseIntegration {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected TestRestTemplate testRestTemplate;
    @Autowired
    @Qualifier("myTestObjectMapper")
    protected ObjectMapper objectMapper;
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;
    @Autowired
    protected WebDriver webDriver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = webAppContextSetup(wac).build();
    }
}
