package hello;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static hello.ResponseHeaderHandler.CUSTOM_HEADER_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GreetingControllerIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}")
    public String greetingEndpointUrl;
    @org.springframework.beans.factory.annotation.Value("${greetingWithNameEndpointUrl}")
    public String greetingWithNameEndpointUrl;

    @Test
    public void greeting_shouldReturnDefaultGreeting_givenNameIsNotInQueryParam() {
        Greeting result = restTemplate.getForObject(greetingEndpointUrl, Greeting.class);
        assertNotNull(result.getId());
        assertEquals("Hello, World!", result.getContent());
    }

    @Test
    public void greeting_shouldReturnGreetingWithName_givenNameInQueryParam() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "Harish");
        Greeting result = restTemplate.getForObject(greetingWithNameEndpointUrl, Greeting.class, queryParams);
        assertNotNull(result.getId());
        assertEquals("Hello, Harish!", result.getContent());
    }

    @Test
    public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        String customHeaderValue = "someValue";
        requestHeaders.add(CUSTOM_HEADER_NAME, customHeaderValue);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", "Harish");
        ResponseEntity<Greeting> response = restTemplate.exchange(greetingWithNameEndpointUrl, HttpMethod.GET, requestEntity, Greeting.class, queryParams);
        Greeting result = response.getBody();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Hello, Harish!", result.getContent());
        assertEquals(customHeaderValue, response.getHeaders().getFirst(CUSTOM_HEADER_NAME));

    }

}
