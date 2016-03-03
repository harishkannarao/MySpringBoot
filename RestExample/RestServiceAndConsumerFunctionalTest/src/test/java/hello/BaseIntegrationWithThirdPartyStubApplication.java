package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationThirdPartyRestServiceStubApplication.class})
@WebIntegrationTest({
        "server.port=${RANDOM_PORT_1:8182}",
        "management.port=${RANDOM_PORT_2:8183}",
        "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
})
public abstract class BaseIntegrationWithThirdPartyStubApplication {
}
