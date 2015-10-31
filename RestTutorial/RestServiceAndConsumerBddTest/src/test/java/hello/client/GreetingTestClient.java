package hello.client;

import hello.Greeting;
import hello.helper.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GreetingTestClient {
    private String greetingEndpointStringFormat;
    private String greetingWithNameEndpointStringFormat;
    private TestHelper testHelper;
    private RestTemplate restTemplate;

    @Autowired
    public GreetingTestClient(@org.springframework.beans.factory.annotation.Value("${greetingEndpointStringFormat}") String greetingEndpointStringFormat,
                              @org.springframework.beans.factory.annotation.Value("${greetingWithNameEndpointStringFormat}") String greetingWithNameEndpointStringFormat,
                              TestHelper testHelper,
                              @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.greetingEndpointStringFormat = greetingEndpointStringFormat;
        this.greetingWithNameEndpointStringFormat = greetingWithNameEndpointStringFormat;
        this.testHelper = testHelper;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Greeting> getGreeting() {
        return restTemplate.getForEntity(getGreetingEndpointString(), Greeting.class);
    }

    public ResponseEntity<Greeting> getGreetingWithName(String name) {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "Harish");
        return restTemplate.getForEntity(getGreetingWithNameEndpointString(), Greeting.class, queryParams);
    }

    private String getGreetingEndpointString() {
        return String.format(greetingEndpointStringFormat, testHelper.getPort());
    }

    private String getGreetingWithNameEndpointString() {
        return String.format(greetingWithNameEndpointStringFormat, testHelper.getPort());
    }
}
