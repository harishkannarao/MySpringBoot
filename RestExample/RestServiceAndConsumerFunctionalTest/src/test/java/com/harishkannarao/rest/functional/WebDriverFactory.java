package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.util.WebDriverScreenShotUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class WebDriverFactory {

    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class.getName());
    private static final List<WebDriver> WEB_DRIVERS = new ArrayList<>();

    public WebDriver newWebDriver() {
        WebDriver webDriver = createChromeWebDriver();
        WEB_DRIVERS.add(webDriver);
        return webDriver;
    }

    private WebDriver createChromeWebDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver webDriver = new ChromeDriver(getDefaultChromeOptions());
        webDriver.manage().timeouts()
                .pageLoadTimeout(Duration.ofMinutes(3));
        return webDriver;
    }

    public void takeScreenShots(String displayName) {
        IntStream.range(0, WEB_DRIVERS.size())
                .forEach(index -> {
                    String filename = displayName + "_" + index;
                    WebDriverScreenShotUtil.takeScreenShot(WEB_DRIVERS.get(index), filename);
                });
    }

    public void closeAllWebDrivers() {
        WEB_DRIVERS.forEach(WebDriver::close);
        WEB_DRIVERS.forEach(webDriver -> {
            try {
                webDriver.quit();
            } catch (Exception e) {
                LOGGER.warning("Exception while closing WebDriver: " + e.getMessage());
            }
        });
        WEB_DRIVERS.clear();
    }

    private ChromeOptions getDefaultChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        List<String> arguments = new ArrayList<>();
        arguments.add("--allow-insecure-localhost");
        arguments.add("--start-maximized");
        arguments.add("--disable-gpu");
        arguments.add("--no-sandbox");
        arguments.add("--disable-dev-shm-usage");
        boolean isChromeHeadlessOn = Boolean.parseBoolean(System.getProperty("chromeHeadless", "false"));
        if (isChromeHeadlessOn) {
            arguments.add("--headless");
        }
        chromeOptions.addArguments(arguments);
        Optional<String> chromeBinary = Optional.ofNullable(System.getProperty("chromeBinary"));
        chromeBinary.ifPresent(chromeOptions::setBinary);

        return chromeOptions;
    }

}
