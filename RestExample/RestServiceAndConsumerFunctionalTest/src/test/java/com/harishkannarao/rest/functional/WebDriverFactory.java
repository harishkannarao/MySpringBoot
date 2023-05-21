package com.harishkannarao.rest.functional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WebDriverFactory {

    private static final Logger LOGGER = Logger.getLogger(WebDriverFactory.class.getName());
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
                LOGGER.warning("Exception while closing WebDriver: " + e.getMessage());
            }
        });
        WEB_DRIVERS.clear();
    }

}
