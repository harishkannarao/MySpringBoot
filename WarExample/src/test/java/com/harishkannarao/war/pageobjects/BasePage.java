package com.harishkannarao.war.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasePage {
    private WebDriver webDriver;
    private String warExampleBaseUrl;

    @Autowired
    public BasePage(WebDriver webDriver,
                    @Value("${warExample.baseUrl}") String warExampleBaseUrl) {
        this.webDriver = webDriver;
        this.warExampleBaseUrl = warExampleBaseUrl;
    }

    public void navigate() {
        webDriver.navigate().to(warExampleBaseUrl);
        webDriver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("angular-view-div")));
    }
}
