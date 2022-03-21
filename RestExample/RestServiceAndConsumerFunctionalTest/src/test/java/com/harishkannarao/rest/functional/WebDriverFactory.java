package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.util.WebDriverScreenShotUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class WebDriverFactory {

    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class.getName());
    private static final List<WebDriver> WEB_DRIVERS = new ArrayList<>();

    public WebDriver newWebDriver() {
        WebDriver webDriver = createFirefoxDriver();
        WEB_DRIVERS.add(webDriver);
        return webDriver;
    }

    private WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(Boolean.parseBoolean(System.getProperty("headless", "true")));
        WebDriver driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        return driver;
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

}
