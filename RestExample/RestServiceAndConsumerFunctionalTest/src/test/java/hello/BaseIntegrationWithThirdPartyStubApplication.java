package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {TestConfigurationThirdPartyRestServiceStubApplication.class},
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=${RANDOM_PORT_1:8182}",
                "management.port=${RANDOM_PORT_2:8183}",
                "quoteService.url=http://localhost:${server.port}/thirdparty/quote",
                "thirdparty.ping.url=http://localhost:${server.port}/thirdparty/ping"
        })
public abstract class BaseIntegrationWithThirdPartyStubApplication {
}
