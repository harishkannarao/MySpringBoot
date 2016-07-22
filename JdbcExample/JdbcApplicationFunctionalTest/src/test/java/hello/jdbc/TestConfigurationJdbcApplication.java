package hello.jdbc;

import hello.JdbcApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import({JdbcApplication.class})
@PropertySources({
        @PropertySource("classpath:properties/${TEST_ENV:local}-test-config.properties")
})
public class TestConfigurationJdbcApplication {

    @Autowired
    private JsonHeaderInterceptor jsonHeaderInterceptor;

    @Bean
    @Qualifier("myTestRestTemplate")
    public RestTemplate getMyTestRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(jsonHeaderInterceptor);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
