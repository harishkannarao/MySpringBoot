package hello;

import hello.domain.ErrorResponse;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static hello.interceptor.response.ResponseHeaderHandler.CUSTOM_HEADER_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExceptionSimulationControllerIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${generateRuntimeExceptionUrl}")
    public String generateRuntimeExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateCheckedExceptionUrl}")
    public String generateCheckedExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateCustomRuntimeExceptionUrl}")
    public String generateCustomRuntimeExceptionUrl;
    @org.springframework.beans.factory.annotation.Value("${generateCustomCheckedExceptionUrl}")
    public String generateCustomCheckedExceptionUrl;

    @Test
    public void shouldGet500StatusWithMessageForCheckedException() throws Exception {
        try {
            restTemplate.exchange(generateCheckedExceptionUrl, HttpMethod.GET, null, String.class);
            fail("Should throw exception");
        } catch (HttpServerErrorException hsee) {
            assertEquals(500, hsee.getStatusCode().value());
            ErrorResponse errorResponse = objectMapper.readValue(hsee.getResponseBodyAsString(), ErrorResponse.class);
            assertEquals("My Sample Checked Exception", errorResponse.getMessage());
        }
    }

    @Test
    public void shouldGet400StatusWithMessageForRuntimeException() throws Exception {
        try {
            restTemplate.exchange(generateRuntimeExceptionUrl, HttpMethod.GET, null, String.class);
            fail("Should throw exception");
        } catch (HttpClientErrorException hcee) {
            assertEquals(400, hcee.getStatusCode().value());
            ErrorResponse errorResponse = objectMapper.readValue(hcee.getResponseBodyAsString(), ErrorResponse.class);
            assertEquals("My Sample Runtime Exception", errorResponse.getMessage());
        }
    }

    @Test
    public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomRuntimeException() throws Exception {
        try {
            restTemplate.exchange(generateCustomRuntimeExceptionUrl, HttpMethod.GET, null, String.class);
            fail("Should throw exception");
        } catch (HttpClientErrorException hcee) {
            assertEquals(403, hcee.getStatusCode().value());
            ErrorResponse errorResponse = objectMapper.readValue(hcee.getResponseBodyAsString(), ErrorResponse.class);
            assertEquals("CustomRuntime:My Custom Runtime Exception", errorResponse.getMessage());
            assertEquals("CustomRuntime", errorResponse.getCode());
            assertEquals("My Custom Runtime Exception", errorResponse.getDescription());
        }
    }

    @Test
    public void shouldGet403StatusWithMessageCodeAndDescriptionForCustomCheckedException() throws Exception {
        try {
            restTemplate.exchange(generateCustomCheckedExceptionUrl, HttpMethod.GET, null, String.class);
            fail("Should throw exception");
        } catch (HttpClientErrorException hcee) {
            assertEquals(403, hcee.getStatusCode().value());
            ErrorResponse errorResponse = objectMapper.readValue(hcee.getResponseBodyAsString(), ErrorResponse.class);
            assertEquals("CustomChecked:My Custom Checked Exception", errorResponse.getMessage());
            assertEquals("CustomChecked", errorResponse.getCode());
            assertEquals("My Custom Checked Exception", errorResponse.getDescription());
        }
    }

    @Test
    public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        String customHeaderValue = "someValue";
        requestHeaders.add(CUSTOM_HEADER_NAME, customHeaderValue);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        try {
            restTemplate.exchange(generateCustomCheckedExceptionUrl, HttpMethod.GET, requestEntity, String.class);
            fail("Should throw exception");
        } catch (HttpClientErrorException hcee) {
            assertEquals(403, hcee.getStatusCode().value());
            assertEquals(customHeaderValue, hcee.getResponseHeaders().getFirst(CUSTOM_HEADER_NAME));
        }
    }

}
