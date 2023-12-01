package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harishkannarao.rest.controller.CustomApplicationErrorController;
import com.harishkannarao.rest.rule.LogbackTestAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorPageIT extends BaseIntegration {

    private static final String ERROR_STATUS_ID = "errorStatus";
    @Value("${nonExistentPageUrl}")
    private String nonExistentPageUrl;
    @Value("${simulateFilterErrorUrl}")
    private String simulateFilterErrorUrl;
    @Value("${customErrorSimulationUrl}")
    private String customErrorSimulationUrl;

    public final LogbackTestAppender logbackTestAppender = new LogbackTestAppender(CustomApplicationErrorController.class.getName());

    @BeforeEach
    public void setUp() {
        logbackTestAppender.startLogsCapture();
    }

    @AfterEach
    public void tearDown() {
        logbackTestAppender.stopLogsCapture();
    }

    @Test
    public void shouldReturnGeneralErrorPageWith404MessageGivenNonExistentPageForHtmlClients() throws IOException {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(nonExistentPageUrl);
        String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
        assertEquals(nonExistentPageUrl, webDriver.getCurrentUrl());
        assertEquals("404 Not Found", errorStatus);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        logbackTestAppender.assertLogEntry("DEBUG Not Found");
    }

    @Test
    public void shouldReturnGeneralErrorPageWith500MessageGivenNonExistentPageForHtmlClients() throws IOException {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(simulateFilterErrorUrl);
        String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
        assertEquals(simulateFilterErrorUrl, webDriver.getCurrentUrl());
        assertEquals("500 Internal Server Error", errorStatus);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(simulateFilterErrorUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        logbackTestAppender.assertLogEntry("ERROR Internal Server Error");
    }

    @Test
    public void shouldGetNotFoundMessageGivenNonExistentPageForHttpClients() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(nonExistentPageUrl, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorDetails.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorDetails.getError());
        logbackTestAppender.assertLogEntry("DEBUG Not Found");
    }

    @Test
    public void shouldGetInternalServerErrorMessageGivenNonExistentPageForHttpClients() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(simulateFilterErrorUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetails.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorDetails.getError());
        logbackTestAppender.assertLogEntry("ERROR Internal Server Error");
    }

    @Test
    public void shouldHandleCustomExceptionTo405Status() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(customErrorSimulationUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED);
        ErrorDetails errorDetails = objectMapper.readValue(response.getBody(), ErrorDetails.class);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), errorDetails.getStatus());
        assertEquals("ERR123 :: Unique error", errorDetails.getError());
    }


    public static class ErrorDetails {
        @JsonProperty("status")
        private final Integer status;
        @JsonProperty("error")
        private final String error;

        @JsonCreator
        public ErrorDetails(
                @JsonProperty("status") Integer status,
                @JsonProperty("error") String error
        ) {
            this.status = status;
            this.error = error;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }
    }
}
