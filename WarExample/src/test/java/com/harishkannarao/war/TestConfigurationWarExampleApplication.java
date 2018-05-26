package com.harishkannarao.war;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@TestConfiguration
public class TestConfigurationWarExampleApplication {

    @Bean(destroyMethod = "quit")
    public WebDriver getWebDriver(ChromeDriverService chromeDriverService) {
        WebDriver webDriver = new ChromeDriver(chromeDriverService, getDefaultChromeOptions());
        webDriver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
        return webDriver;
    }

    private ChromeOptions getDefaultChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        List<String> arguments = new ArrayList<>();
        arguments.add("--allow-insecure-localhost");
        arguments.add("--start-maximized");
        arguments.add("--disable-gpu");
        arguments.add("--no-sandbox");
        boolean isChromeHeadlessOn = Boolean.parseBoolean(System.getProperty("chromeHeadless", "false"));
        if (isChromeHeadlessOn) {
            arguments.add("--headless");
        }
        chromeOptions.addArguments(arguments);
        Optional<String> chromeBinary = Optional.ofNullable(System.getProperty("chromeBinary"));
        chromeBinary.ifPresent(chromeOptions::setBinary);

        return chromeOptions;
    }

    @Bean(destroyMethod = "stop")
    public ChromeDriverService createChromeDriverService() throws IOException {
        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        Optional<String> chromeDriverBinary = Optional.ofNullable(System.getProperty("chromeDriverBinary"));
        chromeDriverBinary.ifPresent(path -> builder.usingDriverExecutable(new File(path)));

        ChromeDriverService service = builder
                .usingAnyFreePort()
                .build();
        service.start();
        return service;
    }
}
