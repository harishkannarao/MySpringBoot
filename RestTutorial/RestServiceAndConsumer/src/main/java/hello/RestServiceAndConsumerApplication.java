package hello;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestServiceAndConsumerApplication {
    private static ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {
        configurableApplicationContext = SpringApplication.run(RestServiceAndConsumerApplication.class, args);
    }

    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return configurableApplicationContext;
    }

    @Bean
    @Qualifier("myRestTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
