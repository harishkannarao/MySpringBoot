package hello;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationPropertiesApplication.class})
@WebIntegrationTest({
        "server.port=${DEFAULT_PROPERTIES_PORT:8280}",
        "management.port=${DEFAULT_PROPERTIES_MANAGEMENT_PORT:8281}",
        "properties.endpoint.url=http://localhost:${server.port}"
})
public abstract class BaseIntegrationWithDefaultProperties {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate testRestTemplate;
}
