package com.harishkannarao.jdbc;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.web.client.RestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
	classes = {JdbcApplication.class},
	webEnvironment = DEFINED_PORT
)
@Import(value = {
	TestConfigurationJdbcApplication.class,
	DbFixturesPopulator.class,
	TicketTestSupportDao.class
})
@ActiveProfiles(value = {"default", "jdbc-functional-test"})
public abstract class BaseIntegrationJdbc {
	@Autowired
	@Qualifier("myTestRestClient")
	protected RestClient restClient;
	@Autowired
	protected DbFixturesPopulator dbFixturesPopulator;
	@Autowired
	protected WireMock wireMock;
	@Autowired
	private WebDriverFactory webDriverFactory;

	@BeforeEach
	public void setup() {
		dbFixturesPopulator.resetData();

		wireMock.resetMappings();
		wireMock.resetRequests();
		wireMock.resetScenarios();
	}

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
		final int RANDOM_WIREMOCK_PORT = TestSocketUtils.findAvailableTcpPort();
		registry.add("server.port", () -> String.valueOf(RANDOM_SERVER_PORT));
		registry.add("management.port", () -> String.valueOf(RANDOM_MANAGEMENT_PORT));
		registry.add("wiremock.port", () -> String.valueOf(RANDOM_WIREMOCK_PORT));
	}
}
