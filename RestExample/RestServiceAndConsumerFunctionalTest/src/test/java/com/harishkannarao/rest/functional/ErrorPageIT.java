package com.harishkannarao.rest.functional;

import org.junit.Test;
import org.openqa.selenium.By;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ErrorPageIT extends BaseIntegration {
    @org.springframework.beans.factory.annotation.Value("${nonExistentPageUrl}")
    public String nonExistentPageUrl;

    @Test
    public void shouldGetNotFoundErrorPageForNonExistentPage() {
        webDriver.navigate().to(nonExistentPageUrl);
        String errorStatus = webDriver.findElement(By.id("errorStatus")).getText();
        assertEquals(nonExistentPageUrl, webDriver.getCurrentUrl());
        assertEquals("404 Not Found", errorStatus);
    }

    @Test
    public void shouldGetNotFoundStatusForNonExistentPage() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
