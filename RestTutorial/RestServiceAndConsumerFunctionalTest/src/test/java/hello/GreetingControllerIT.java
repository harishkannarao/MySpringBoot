package hello;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GreetingControllerIT extends BaseIntegration {

    public static final String greetingEndpointStringFormat = "http://localhost:%s/greeting/get";
    public static final String greetingWithNameEndpointStringFormat = greetingEndpointStringFormat + "?name={name}";

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
