package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GreetingControllerIT extends BaseIntegration {

    @Autowired
    @org.springframework.beans.factory.annotation.Value("${greetingEndpointStringFormat}")
    public String greetingEndpointStringFormat;
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${greetingWithNameEndpointStringFormat}")
    public String greetingWithNameEndpointStringFormat;

    @Test
    public void greeting_shouldReturnDefaultGreeting_givenNameIsNotInQueryParam() {
        Greeting result = restTemplate.getForObject(getGreetingEndpointString(), Greeting.class);
        assertNotNull(result.getId());
        assertEquals("Hello, World!", result.getContent());
    }

    @Test
    public void greeting_shouldReturnGreetingWithName_givenNameInQueryParam() {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "Harish");
        Greeting result = restTemplate.getForObject(getGreetingWithNameEndpointString(), Greeting.class, queryParams);
        assertNotNull(result.getId());
        assertEquals("Hello, Harish!", result.getContent());
    }

    private String getGreetingEndpointString() {
        return String.format(greetingEndpointStringFormat, port);
    }

    private String getGreetingWithNameEndpointString() {
        return String.format(greetingWithNameEndpointStringFormat, port);
    }

}
