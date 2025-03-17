package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = {
                RestServiceAndConsumerApplication.class,
                TestConfigurationFunctionalTestsWithoutMocks.class
        },
        webEnvironment = DEFINED_PORT,
        properties = {
                "quoteService.url=http://localhost:${server.port}/rest-service/thirdparty/quote"
        })
public abstract class BaseIntegrationWithTestConfiguration {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;

	@DynamicPropertySource
	static void registerTestProperties(DynamicPropertyRegistry registry) {
		final int RANDOM_SERVER_PORT = TestSocketUtils.findAvailableTcpPort();
		final int RANDOM_MANAGEMENT_PORT = TestSocketUtils.findAvailableTcpPort();
		registry.add("server.port", () -> String.valueOf(RANDOM_SERVER_PORT));
		registry.add("management.port", () -> String.valueOf(RANDOM_MANAGEMENT_PORT));
	}

}

