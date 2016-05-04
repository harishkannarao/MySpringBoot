package hello;

import hello.pageobjects.BasePage;
import hello.pageobjects.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
//@EnableAutoConfiguration
//@ComponentScan
@Import({WarExampleApplication.class})
@PropertySources({
        @PropertySource("classpath:properties/local-test-config.properties")
})
public class TestConfigurationWarExampleApplication {

    private static final String FIREFOX_DRIVER = "firefox";
    private static final String PHANTOMJS_DRIVER = "phantomjs";

    @Autowired
    @Value("${warExampleTestDriver}")
    private String warExampleTestDriver;

    @Bean
    public BasePage getBasePage() {
        BasePage basePage = new BasePage(getWebDriver(), "http://localhost:8185/");
        return basePage;
    }

    @Bean
    public HomePage getHomePage() {
        HomePage homePage = new HomePage(getWebDriver());
        return homePage;
    }

    @Bean(destroyMethod = "quit")
    public WebDriver getWebDriver() {
        WebDriver driver = null;
        if (FIREFOX_DRIVER.equals(warExampleTestDriver)) {
            FirefoxProfile fp = new FirefoxProfile();
            fp.setPreference("browser.startup.homepage", "about:blank");
            fp.setPreference("startup.homepage_welcome_url", "about:blank");
            fp.setPreference("startup.homepage_welcome_url.additional", "about:blank");
            driver = new FirefoxDriver(fp);
            driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        } else if (PHANTOMJS_DRIVER.equals(warExampleTestDriver)) {
            DesiredCapabilities phantomjsCapabilities = DesiredCapabilities.phantomjs();
            String [] phantomJsArgs = {"--ignore-ssl-errors=true","--ssl-protocol=any"};
            phantomjsCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
            driver = new PhantomJSDriver(phantomjsCapabilities);
            driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        }

        return driver;
    }

    //To resolve ${} in @Value
    /*@Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }*/
}
