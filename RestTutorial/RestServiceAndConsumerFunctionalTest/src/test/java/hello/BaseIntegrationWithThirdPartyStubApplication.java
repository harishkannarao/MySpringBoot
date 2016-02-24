package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationThirdPartyRestServiceStubApplication.class})
@WebIntegrationTest({
        "server.port=${ADDITIONAL_SERVER_PORT:8182}",
        "management.port=${ADDITIONAL_MANAGEMENT_PORT:8183}",
        "quoteService.url=http://localhost:${server.port}/thirdparty/quote"
})
public abstract class BaseIntegrationWithThirdPartyStubApplication {
}
