package hello;

import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;

public class IndexPageIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${indexPageEndpointUrl}")
    public String indexPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        webDriver.navigate().to(indexPageEndpointUrl);
        String content = webDriver.findElement(By.id("content")).getText();
        assertEquals("Hello World !!!", content);
    }
}
