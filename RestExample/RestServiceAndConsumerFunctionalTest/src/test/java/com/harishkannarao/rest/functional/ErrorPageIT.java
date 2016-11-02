package com.harishkannarao.rest.functional;

import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;

public class ErrorPageIT extends BaseIntegration {
    @org.springframework.beans.factory.annotation.Value("${invalidPageUrl}")
    public String invalidPageUrl;

    @Test
    public void shouldGetErrorPage() {
        webDriver.navigate().to(invalidPageUrl);
        String errorStatus = webDriver.findElement(By.id("errorStatus")).getText();
        assertEquals(invalidPageUrl, webDriver.getCurrentUrl());
        assertEquals("404 Not Found", errorStatus);
    }
}
