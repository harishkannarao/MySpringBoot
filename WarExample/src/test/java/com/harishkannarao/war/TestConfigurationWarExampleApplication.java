package com.harishkannarao.war;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfigurationWarExampleApplication {

    @Bean(destroyMethod = "quit")
    public WebDriver getWebDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(Boolean.parseBoolean(System.getProperty("headless", "true")));
        WebDriver driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        return driver;
    }

}
