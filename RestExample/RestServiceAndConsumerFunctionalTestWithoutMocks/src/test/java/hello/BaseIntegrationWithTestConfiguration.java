package hello;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfiguration.class})
@WebIntegrationTest({
        "server.port=8180",
        "management.port=8181",
        "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
})
public abstract class BaseIntegrationWithTestConfiguration {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;
}

