package com.harishkannarao.jdbc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WebDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final List<WebDriver> WEB_DRIVERS = new ArrayList<>();

    public WebDriver newWebDriver() {
        WebDriver webDriver = createHtmlUnitDriver();
        WEB_DRIVERS.add(webDriver);
        return webDriver;
    }

    private WebDriver createHtmlUnitDriver() {
        return new HtmlUnitDriver();
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
    }

}
