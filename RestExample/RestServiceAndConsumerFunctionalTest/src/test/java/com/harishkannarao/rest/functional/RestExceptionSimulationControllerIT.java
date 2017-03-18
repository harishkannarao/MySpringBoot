package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.ErrorResponse;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.harishkannarao.rest.filter.ResponseHeaderFilter.CUSTOM_HEADER_NAME;
import static org.junit.Assert.assertEquals;

public class RestExceptionSimulationControllerIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${generateRestRuntimeExceptionUrl}")
    public String generateRestRuntimeExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateRestCheckedExceptionUrl}")
    public String generateRestCheckedExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateRestCustomRuntimeExceptionUrl}")
    public String generateRestCustomRuntimeExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateRestCustomCheckedExceptionUrl}")
    public String generateRestCustomCheckedExceptionUrl;

    @Test
    public void shouldGet500StatusWithMessageForCheckedException() throws Exception {
        ResponseEntity<String> response = testRestTemplate.exchange(generateRestCheckedExceptionUrl, HttpMethod.GET, null, String.class);
        assertEquals(500, response.getStatusCodeValue());
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        assertEquals("My Sample Checked Exception", errorResponse.getMessage());
    }

    @Test
    public void shouldGet400StatusWithMessageForRuntimeException() throws Exception {
        ResponseEntity<String> response = testRestTemplate.exchange(generateRestRuntimeExceptionUrl, HttpMethod.GET, null, String.class);
        assertEquals(400, response.getStatusCodeValue());
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        assertEquals("My Sample Runtime Exception", errorResponse.getMessage());
    }

    @Test
    public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomRuntimeException() throws Exception {
        ResponseEntity<String> response = testRestTemplate.exchange(generateRestCustomRuntimeExceptionUrl, HttpMethod.GET, null, String.class);
        assertEquals(403, response.getStatusCodeValue());
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        assertEquals("CustomRuntime:My Custom Runtime Exception", errorResponse.getMessage());
        assertEquals("CustomRuntime", errorResponse.getCode());
        assertEquals("My Custom Runtime Exception", errorResponse.getDescription());
    }

    @Test
    public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomCheckedException() throws Exception {
        ResponseEntity<String> response = testRestTemplate.exchange(generateRestCustomCheckedExceptionUrl, HttpMethod.GET, null, String.class);
        assertEquals(403, response.getStatusCodeValue());
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        assertEquals("CustomChecked:My Custom Checked Exception", errorResponse.getMessage());
        assertEquals("CustomChecked", errorResponse.getCode());
        assertEquals("My Custom Checked Exception", errorResponse.getDescription());
    }

    @Test
    public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        String customHeaderValue = "someValue";
        requestHeaders.add(CUSTOM_HEADER_NAME, customHeaderValue);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(generateRestCustomCheckedExceptionUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals(customHeaderValue, response.getHeaders().getFirst(CUSTOM_HEADER_NAME));
    }

}
