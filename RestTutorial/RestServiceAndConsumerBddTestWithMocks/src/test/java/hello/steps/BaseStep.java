package hello.steps;

import hello.config.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = {TestConfiguration.class}, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseStep {

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;
}

