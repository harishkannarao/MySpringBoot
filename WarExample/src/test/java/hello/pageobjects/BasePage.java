package hello.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    private WebDriver webDriver;
    private String warExampleBaseUrl;

    public BasePage(WebDriver webDriver, String warExampleBaseUrl) {
        this.webDriver = webDriver;
        this.warExampleBaseUrl = warExampleBaseUrl;
    }


    public void navigate() {
//        webDriver.navigate().to(warExampleBaseUrl);
//        webDriver.navigate().refresh();
        webDriver.get(warExampleBaseUrl);
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        WebElement indexViewContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("angular-view-div")));
    }
}
