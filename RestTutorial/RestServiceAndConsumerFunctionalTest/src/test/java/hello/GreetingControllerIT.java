package hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestServiceAndConsumerApplication.class)
@WebIntegrationTest
public class GreetingControllerIT {

    RestTemplate restTemplate = new RestTemplate();

    public static final String greetingEndpoint = "http://localhost:8080/greeting";
    public static final String greetingWithNameEndpoint = greetingEndpoint + "?name={name}";

    @Test
    public void greeting_shouldReturnDefaultGreeting_givenNameIsNotInQueryParam() {
        Greeting result = restTemplate.getForObject(greetingEndpoint, Greeting.class);
        assertNotNull(result.getId());
        assertEquals("Hello, World!", result.getContent());
    }

    @Test
    public void greeting_shouldReturnGreetingWithName_givenNameInQueryParam() {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "Harish");
        Greeting result = restTemplate.getForObject(greetingWithNameEndpoint, Greeting.class, queryParams);
        assertNotNull(result.getId());
        assertEquals("Hello, Harish!", result.getContent());
    }

}
