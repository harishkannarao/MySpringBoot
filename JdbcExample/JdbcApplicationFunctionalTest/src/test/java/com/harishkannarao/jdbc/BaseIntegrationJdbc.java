package com.harishkannarao.jdbc;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {
                JdbcApplication.class,
                TestConfigurationJdbcApplication.class
        },
        webEnvironment = DEFINED_PORT
)
@ActiveProfiles(value = {"default", "jdbc-functional-test"})
public abstract class BaseIntegrationJdbc {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;
    @Autowired
    protected DbFixturesPopulator dbFixturesPopulator;
    @Autowired
    protected WireMock wireMock;
    @Autowired
    private WebDriverFactory webDriverFactory;

    @Before
    public void setup() {
        dbFixturesPopulator.resetData();

        wireMock.resetMappings();
        wireMock.resetRequests();
        wireMock.resetScenarios();
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            super.failed(e, description);
            webDriverFactory.takeScreenShots(description.getDisplayName(), false);
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            webDriverFactory.takeScreenShots(description.getDisplayName(), true);
            webDriverFactory.closeAllWebDrivers();
        }
    };

    protected WebDriver newWebDriver() {
        return webDriverFactory.newWebDriver();
    }
}
