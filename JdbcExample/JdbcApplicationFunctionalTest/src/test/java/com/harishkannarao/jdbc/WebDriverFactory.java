package com.harishkannarao.jdbc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class WebDriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);
    private final List<WebDriver> webDrivers = new ArrayList<>();

    public WebDriver newWebDriver() {
        WebDriver webDriver = createHtmlUnitDriver();
        webDrivers.add(webDriver);
        return webDriver;
    }

    private WebDriver createHtmlUnitDriver() {
        return new HtmlUnitDriver();
    }

    public void closeAllWebDrivers() {
        webDrivers.forEach(WebDriver::close);
        webDrivers.forEach(webDriver -> {
            try {
                webDriver.quit();
            } catch (Exception e) {
                LOGGER.warn("Exception while closing WebDriver: " + e.getMessage());
            }
        });
        webDrivers.clear();
    }

}
