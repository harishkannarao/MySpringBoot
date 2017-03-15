package com.harishkannarao.rest.steps;

import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import com.harishkannarao.rest.config.BddTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=8180",
                "management.port=8181",
                "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
        }
)
@ContextConfiguration(classes = {RestServiceAndConsumerApplication.class, BddTestConfiguration.class})
public abstract class BaseStep {
}

