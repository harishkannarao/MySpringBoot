package com.harishkannarao.rest.functional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WebDriverFactory {

    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class.getName());
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
                LOGGER.warning("Exception while closing WebDriver: " + e.getMessage());
            }
        });
        webDrivers.clear();
    }

}
