package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.controller.ApplicationErrorController;
import com.harishkannarao.rest.controller.ApplicationErrorController.ErrorDetails;
import com.harishkannarao.rest.rule.LogbackTestAppenderRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ErrorPageIT extends BaseIntegration {
    private static final String LOGGING_PATTERN = "%-5level %message%n";
    private static final String LOG_FILE_LOCATION = "target/logs";
    private static final String LOG_FILE_PREFIX = "test_log_";

    private static final String ERROR_STATUS_ID = "errorStatus";
    @org.springframework.beans.factory.annotation.Value("${nonExistentPageUrl}")
    private String nonExistentPageUrl;
    @org.springframework.beans.factory.annotation.Value("${simulateFilterErrorUrl}")
    private String simulateFilterErrorUrl;

    @Rule
    public final LogbackTestAppenderRule testAppenderRule = new LogbackTestAppenderRule(ApplicationErrorController.class.getName(), LOGGING_PATTERN, LOG_FILE_LOCATION, LOG_FILE_PREFIX);

    @Test
    public void shouldReturnGeneralErrorPageWith404MessageGivenNonExistentPageForHtmlClients() throws IOException {
        webDriver.navigate().to(nonExistentPageUrl);
        String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
        assertEquals(nonExistentPageUrl, webDriver.getCurrentUrl());
        assertEquals("404 Not Found", errorStatus);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        testAppenderRule.assertLogEntry("DEBUG Not Found");
    }

    @Test
    public void shouldReturnGeneralErrorPageWith500MessageGivenNonExistentPageForHtmlClients() throws IOException {
        webDriver.navigate().to(simulateFilterErrorUrl);
        String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
        assertEquals(simulateFilterErrorUrl, webDriver.getCurrentUrl());
        assertEquals("500 Internal Server Error", errorStatus);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(simulateFilterErrorUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        testAppenderRule.assertLogEntry("ERROR Internal Server Error");
    }

    @Test
    public void shouldGetNotFoundMessageGivenNonExistentPageForHttpClients() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorDetails.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorDetails.getError());
        testAppenderRule.assertLogEntry("DEBUG Not Found");
    }

    @Test
    public void shouldGetInternalServerErrorMessageGivenNonExistentPageForHttpClients() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(simulateFilterErrorUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetails.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorDetails.getError());
        testAppenderRule.assertLogEntry("ERROR Internal Server Error");
    }

}
