package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationWarExampleApplication.class})
@WebIntegrationTest({
        "server.port=${RANDOM_PORT_5:8185}",
        "spring.config.location=classpath:/spring-boot-default-config/"
})
public abstract class AbstractBaseIT {
/*
    private static ConfigurableApplicationContext applicationContext = null;

    @BeforeClass
    public static void startApplication() {
        String[] args = new String[2];
        args[0] = "--spring.config.location=classpath*:/spring-boot-default-config/";
        args[1] = "--server.port=${RANDOM_PORT_5:8185}";
        applicationContext = SpringApplication.run(WarExampleApplication.class, args);
    }

    @AfterClass
    public static void stopApplication() {
        if(applicationContext != null) {
            applicationContext.close();
        }
    }*/
}
