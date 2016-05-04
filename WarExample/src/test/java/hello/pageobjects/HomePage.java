package hello.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver webDriver;

    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public boolean isOnPage() {
        return webDriver.findElements(By.id("qa-home-page-id")).size() > 0;
    }
}
