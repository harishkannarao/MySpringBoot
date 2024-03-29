package com.harishkannarao.rest.functional;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexPageIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${indexPageEndpointUrl}")
    public String indexPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(indexPageEndpointUrl);
        String content = webDriver.findElement(By.id("content")).getText();
        assertEquals("Hello World !!!", content);
    }
}
