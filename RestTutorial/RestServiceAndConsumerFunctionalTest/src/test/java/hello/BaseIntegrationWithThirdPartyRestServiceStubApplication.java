package hello;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ThirdPartyRestServiceStubApplication.class})
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseIntegrationWithThirdPartyRestServiceStubApplication {
    @Autowired
    protected EmbeddedWebApplicationContext server;
    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
}
