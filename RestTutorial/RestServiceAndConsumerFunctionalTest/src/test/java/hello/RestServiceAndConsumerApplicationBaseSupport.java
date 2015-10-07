package hello;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestServiceAndConsumerApplication.class)
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public class RestServiceAndConsumerApplicationBaseSupport {
    @Autowired
    protected EmbeddedWebApplicationContext server;

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;

    protected RestTemplate restTemplate = new RestTemplate();
}
