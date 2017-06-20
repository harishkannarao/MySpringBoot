package com.harishkannarao.rest.functional;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.harishkannarao.rest.filter.ResponseHeaderFilter.CUSTOM_HEADER_NAME;
import static org.junit.Assert.assertEquals;

public class HtmlExceptionSimulationControllerIT extends BaseIntegration {

    private static final String ERROR_MESSAGE_ID = "errorMessage";
    @org.springframework.beans.factory.annotation.Value("${generateHtmlRuntimeExceptionUrl}")
    private String generateHtmlRuntimeExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateHtmlCheckedExceptionUrl}")
    private String generateHtmlCheckedExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateHtmlCustomRuntimeExceptionUrl}")
    private String generateHtmlCustomRuntimeExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateHtmlCustomCheckedExceptionUrl}")
    private String generateHtmlCustomCheckedExceptionUrl;

    @Test
    public void shouldGet500StatusWithMessageForCheckedException() throws Exception {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(generateHtmlCheckedExceptionUrl);
        String errorMessage = webDriver.findElement(By.id(ERROR_MESSAGE_ID)).getText();
        assertEquals(generateHtmlCheckedExceptionUrl, webDriver.getCurrentUrl());
        assertEquals("My Sample Checked Exception", errorMessage);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(generateHtmlCheckedExceptionUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldGet400StatusWithMessageForRuntimeException() throws Exception {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(generateHtmlRuntimeExceptionUrl);
        String errorMessage = webDriver.findElement(By.id(ERROR_MESSAGE_ID)).getText();
        assertEquals(generateHtmlRuntimeExceptionUrl, webDriver.getCurrentUrl());
        assertEquals("My Sample Runtime Exception", errorMessage);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(generateHtmlRuntimeExceptionUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomRuntimeException() throws Exception {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(generateHtmlCustomRuntimeExceptionUrl);
        String errorMessage = webDriver.findElement(By.id(ERROR_MESSAGE_ID)).getText();
        assertEquals(generateHtmlCustomRuntimeExceptionUrl, webDriver.getCurrentUrl());
        assertEquals("CustomRuntime:My Custom Runtime Exception", errorMessage);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(generateHtmlCustomRuntimeExceptionUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomCheckedException() throws Exception {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(generateHtmlCustomCheckedExceptionUrl);
        String errorMessage = webDriver.findElement(By.id(ERROR_MESSAGE_ID)).getText();
        assertEquals(generateHtmlCustomCheckedExceptionUrl, webDriver.getCurrentUrl());
        assertEquals("CustomChecked:My Custom Checked Exception", errorMessage);

        ResponseEntity<String> response = testRestTemplateForHtml.getForEntity(generateHtmlCustomCheckedExceptionUrl, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        String customHeaderValue = "someValue";
        requestHeaders.add(CUSTOM_HEADER_NAME, customHeaderValue);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity<String> response = testRestTemplateForHtml.exchange(generateHtmlCustomCheckedExceptionUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals(customHeaderValue, response.getHeaders().getFirst(CUSTOM_HEADER_NAME));
    }
}
