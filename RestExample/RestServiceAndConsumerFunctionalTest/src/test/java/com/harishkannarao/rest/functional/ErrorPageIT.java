package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.controller.ApplicationErrorController.ErrorDetails;
import org.junit.Test;
import org.openqa.selenium.By;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ErrorPageIT extends BaseIntegration {
    private static final String ERROR_STATUS_ID = "errorStatus";
    @org.springframework.beans.factory.annotation.Value("${nonExistentPageUrl}")
    private String nonExistentPageUrl;
    @org.springframework.beans.factory.annotation.Value("${simulateFilterErrorUrl}")
    private String simulateFilterErrorUrl;

    @Test
    public void shouldReturnGeneralErrorPageWith404MessageGivenNonExistentPageForHtmlClients() {
        webDriver.navigate().to(nonExistentPageUrl);
        String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
        assertEquals(nonExistentPageUrl, webDriver.getCurrentUrl());
        assertEquals("404 Not Found", errorStatus);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnGeneralErrorPageWith500MessageGivenNonExistentPageForHtmlClients() {
        webDriver.navigate().to(simulateFilterErrorUrl);
        String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
        assertEquals(simulateFilterErrorUrl, webDriver.getCurrentUrl());
        assertEquals("500 Internal Server Error", errorStatus);

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
