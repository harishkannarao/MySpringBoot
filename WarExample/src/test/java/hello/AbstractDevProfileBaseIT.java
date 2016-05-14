package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationWarExampleApplication.class})
@WebIntegrationTest({
        "server.port=${RANDOM_PORT_5:8186}",
        "spring.config.location=classpath:/spring-boot-default-config/",
        "spring.profiles.active=dev"
})
public abstract class AbstractDevProfileBaseIT {}
