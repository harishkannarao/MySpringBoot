package com.harishkannarao.rest.functional;

import org.openqa.selenium.WebDriver;

public class WebDriverWrapper {

    private final String testDisplayName;
    private final WebDriver webDriver;

    public WebDriverWrapper(String testDisplayName, WebDriver webDriver) {
        this.testDisplayName = testDisplayName;
        this.webDriver = webDriver;
    }

    public String getTestDisplayName() {
        return testDisplayName;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}
