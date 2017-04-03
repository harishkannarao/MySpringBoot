package com.harishkannarao.war.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HomePageDevProfile {
    private final WebDriver webDriver;
    private final String homePageDevProfileUrl;

    @Autowired
    public HomePageDevProfile(WebDriver webDriver,
                              @Value("${warExample.indexPageUrl}") String warExampleIndexPageUrl) {
        this.webDriver = webDriver;
        this.homePageDevProfileUrl = warExampleIndexPageUrl + "#/home";
    }

    public boolean isOnPage() {
        return webDriver.findElements(By.id("qa-home-page-id")).size() > 0;
    }

    public void navigate() {
        webDriver.navigate().to(homePageDevProfileUrl);
        webDriver.navigate().refresh();
        WebDriverWait waitForHomePageElement = new WebDriverWait(webDriver, 10);
        waitForHomePageElement.until(ExpectedConditions.presenceOfElementLocated(By.id("qa-home-page-id")));
    }

    public String getMessage1() {
        return webDriver.findElement(By.id("message1")).getText();
    }
    public String getMessage2() {
        return webDriver.findElement(By.id("message2")).getText();
    }
    public String getMessage3() {
        return webDriver.findElement(By.id("message3")).getText();
    }
    public String getMessage4() {
        return webDriver.findElement(By.id("message4")).getText();
    }
    public String getMessage5() {
        return webDriver.findElement(By.id("message5")).getText();
    }
}
