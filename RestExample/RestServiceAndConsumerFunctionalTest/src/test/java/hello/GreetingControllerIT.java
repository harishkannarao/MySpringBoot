package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GreetingControllerIT extends BaseIntegration {

    @Autowired
    @org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}")
    public String greetingEndpointUrl;
    @Autowired
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
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "Harish");
        Greeting result = restTemplate.getForObject(greetingWithNameEndpointUrl, Greeting.class, queryParams);
        assertNotNull(result.getId());
        assertEquals("Hello, Harish!", result.getContent());
    }

}
