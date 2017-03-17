package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.controller.ApplicationErrorController.ErrorDetails;
import org.junit.Test;
import org.openqa.selenium.By;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ErrorPageIT extends BaseIntegration {
    @org.springframework.beans.factory.annotation.Value("${nonExistentPageUrl}")
    public String nonExistentPageUrl;
    @org.springframework.beans.factory.annotation.Value("${simulateFilterErrorUrl}")
    public String simulateFilterErrorUrl;

    @Test
    public void shouldReturnGeneralErrorPageWith404MessageGivenNonExistentPageForHtmlClients() {
        webDriver.navigate().to(nonExistentPageUrl);
        String errorStatus = webDriver.findElement(By.id("errorStatus")).getText();
        assertEquals(nonExistentPageUrl, webDriver.getCurrentUrl());
        assertEquals("404 Not Found", errorStatus);
    }

    @Test
    public void shouldGetNotFoundStatusGivenNonExistentPageForHtmlClients() {
        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnGeneralErrorPageWith500MessageGivenNonExistentPageForHtmlClients() {
        webDriver.navigate().to(simulateFilterErrorUrl);
        String errorStatus = webDriver.findElement(By.id("errorStatus")).getText();
        assertEquals(simulateFilterErrorUrl, webDriver.getCurrentUrl());
        assertEquals("500 Internal Server Error", errorStatus);
    }

    @Test
    public void shouldGetServerErrorStatusGivenNonExistentPageForHtmlClients() {
        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(simulateFilterErrorUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldGetNotFoundMessageGivenNonExistentPageForHttpClients() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorDetails.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorDetails.getError());
    }

    @Test
    public void shouldGetInternalServerErrorMessageGivenNonExistentPageForHttpClients() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(simulateFilterErrorUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetails.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorDetails.getError());
    }

}
