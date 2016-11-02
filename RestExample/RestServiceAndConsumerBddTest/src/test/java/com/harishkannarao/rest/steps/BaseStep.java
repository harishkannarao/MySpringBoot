package com.harishkannarao.rest.steps;

import com.harishkannarao.rest.config.TestConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfiguration.class}, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({
        "server.port=8180",
        "management.port=8181",
        "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
})
public abstract class BaseStep {
}

