package hello;

import org.junit.Test;
import org.openqa.selenium.By;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class HelloPageIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${helloPageEndpointUrl}")
    public String helloPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        webDriver.navigate().to(helloPageEndpointUrl);
        String date = webDriver.findElement(By.id("date")).getText();
        String message = webDriver.findElement(By.id("message")).getText();

        assertEquals(LocalDate.now().toString(), date);
        assertEquals("Hello Harish", message);
    }

}
