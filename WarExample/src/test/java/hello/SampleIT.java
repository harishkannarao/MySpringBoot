package hello;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SampleIT extends AbstractBaseIT{

    @Autowired
    @Value("${warExampleBaseUrl}")
    private String warExampleBaseUrl;

    @Test
    public void simpleTest() {
        DesiredCapabilities phantomjsCapabilities = DesiredCapabilities.phantomjs();
        String [] phantomJsArgs = {"--ignore-ssl-errors=true","--ssl-protocol=any"};
        phantomjsCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
        WebDriver driver = new PhantomJSDriver(phantomjsCapabilities);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.get(warExampleBaseUrl+"/app/index.html");
        driver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement angularViewElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("angular-view-div")));
        assertTrue(angularViewElement.isDisplayed());
        WebElement message3 = driver.findElement(By.id("message3"));
        assertEquals("Message 3", message3.getText());
    }
}
