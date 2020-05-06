package com.harishkannarao.jdbc;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
@PropertySources({
        @PropertySource("classpath:properties/${test.env:local}-test-config.properties")
})
public class TestConfigurationJdbcApplication {

    @Autowired
    private JsonHeaderInterceptor jsonHeaderInterceptor;

    @Value("${wiremock.port}")
    int wireMockPort;

    @Bean(destroyMethod = "stop")
    public WireMockServer createWireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(options().port(wireMockPort));
        wireMockServer.start();
        return wireMockServer;
    }

    @Bean
    public WireMock createWireMockClient(WireMockServer wireMockServer) {
        return new WireMock(wireMockServer.port());
    }

    @Bean
    @Qualifier("myTestRestTemplate")
    public RestTemplate getMyTestRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(jsonHeaderInterceptor);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public WebDriverFactory createWebDriverFactorySingleton() {
        return new WebDriverFactory();
    }
}
