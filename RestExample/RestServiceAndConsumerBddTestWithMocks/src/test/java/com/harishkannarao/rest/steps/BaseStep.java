package com.harishkannarao.rest.steps;

import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import com.harishkannarao.rest.config.BddWithMocksTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=8180",
                "management.port=8181"
        }
)
@ContextConfiguration(classes = {RestServiceAndConsumerApplication.class, BddWithMocksTestConfiguration.class})
public abstract class BaseStep {
}

