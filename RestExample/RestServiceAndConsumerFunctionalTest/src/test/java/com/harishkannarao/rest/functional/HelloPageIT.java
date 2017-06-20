package com.harishkannarao.rest.functional;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static com.harishkannarao.rest.filter.ResponseHeaderFilter.CUSTOM_HEADER_NAME;
import static org.junit.Assert.assertEquals;

public class HelloPageIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${helloPageEndpointUrl}")
    public String helloPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        WebDriver webDriver = newWebDriver();
        webDriver.navigate().to(helloPageEndpointUrl);
        String date = webDriver.findElement(By.id("date")).getText();
        String message = webDriver.findElement(By.id("message")).getText();

        assertEquals(LocalDate.now().toString(), date);
        assertEquals("Hello Harish", message);
    }

    @Test
    public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        String customHeaderValue = "someValue";
        requestHeaders.add(CUSTOM_HEADER_NAME, customHeaderValue);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity<String> response = testRestTemplateForHtml.exchange(helloPageEndpointUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customHeaderValue, response.getHeaders().getFirst(CUSTOM_HEADER_NAME));
    }
}
