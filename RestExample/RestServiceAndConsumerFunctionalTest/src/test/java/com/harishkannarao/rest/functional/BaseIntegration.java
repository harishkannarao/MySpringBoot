package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.rest.RestServiceAndConsumerApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.TestSocketUtils;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
        classes = {
                RestServiceAndConsumerApplication.class,
                TestConfigurationRestServiceAndConsumerApplication.class
        },
        webEnvironment = DEFINED_PORT,
        properties = {
                "thirdparty.ping.url=http://localhost:${server.port}/rest-service/thirdparty/ping",
                "quoteService.url=http://localhost:${server.port}/rest-service/thirdparty/quote"
        })
public abstract class BaseIntegration {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected TestRestTemplate testRestTemplate;
    @Autowired
    @Qualifier("myTestRestTemplateForHtml")
    protected TestRestTemplate testRestTemplateForHtml;
    @Autowired
    @Qualifier("myTestObjectMapper")
    protected ObjectMapper objectMapper;
    @Autowired
    protected ConfigurableEnvironment configurableEnvironment;
    @Autowired
    private WebDriverFactory webDriverFactory;

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        webDriverFactory.closeAllWebDrivers();
    }

    protected WebDriver newWebDriver() {
        return webDriverFactory.newWebDriver();
    }

	@DynamicPropertySource
	static void registerTestProperties(DynamicPropertyRegistry registry) {
		final int RANDOM_SERVER_PORT = TestSocketUtils.findAvailableTcpPort();
		final int RANDOM_MANAGEMENT_PORT = TestSocketUtils.findAvailableTcpPort();
		registry.add("server.port", () -> String.valueOf(RANDOM_SERVER_PORT));
		registry.add("management.port", () -> String.valueOf(RANDOM_MANAGEMENT_PORT));
	}

}
