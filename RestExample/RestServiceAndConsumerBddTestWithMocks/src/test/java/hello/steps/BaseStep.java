package hello.steps;

import hello.config.TestConfiguration;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfiguration.class}, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({
        "server.port=8180",
        "management.port=8181"
})
public abstract class BaseStep {
}

