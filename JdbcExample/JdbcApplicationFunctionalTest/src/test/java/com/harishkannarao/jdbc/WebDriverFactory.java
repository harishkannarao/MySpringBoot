package com.harishkannarao.jdbc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class WebDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final List<WebDriver> WEB_DRIVERS = new ArrayList<>();

    private final ChromeDriverService chromeDriverService = createChromeDriverService();

    public WebDriver newWebDriver() {
        startChromeDriverService();
        WebDriver webDriver = createChromeWebDriver();
        WEB_DRIVERS.add(webDriver);
        return webDriver;
    }

    private WebDriver createChromeWebDriver() {
        WebDriver webDriver = new ChromeDriver(chromeDriverService, getDefaultChromeOptions());
        webDriver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
        return webDriver;
    }

    public void takeScreenShots(String displayName, boolean success) {
        IntStream.range(0, WEB_DRIVERS.size())
                .forEach(index -> {
                    String filename = displayName + "_" + index;
                    WebDriverScreenShotUtil.takeScreenShot(WEB_DRIVERS.get(index), filename, success);
                });
    }

    public void closeAllWebDrivers() {
        WEB_DRIVERS.forEach(WebDriver::close);
        WEB_DRIVERS.forEach(webDriver -> {
            try {
                webDriver.quit();
            } catch (Exception e) {
                LOGGER.warn("Exception while closing WebDriver: " + e.getMessage());
            }
        });
        WEB_DRIVERS.clear();
        stopChromeDriverService();
    }

    private void startChromeDriverService() {
        if (!chromeDriverService.isRunning()) {
            try {
                chromeDriverService.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void stopChromeDriverService() {
        if (chromeDriverService.isRunning()) {
            chromeDriverService.stop();
        }
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

    private ChromeDriverService createChromeDriverService() {
        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        Optional<String> chromeDriverBinary = Optional.ofNullable(System.getProperty("chromeDriverBinary"));
        chromeDriverBinary.ifPresent(path -> builder.usingDriverExecutable(new File(path)));

        return builder
                .usingAnyFreePort()
                .build();
    }

}
