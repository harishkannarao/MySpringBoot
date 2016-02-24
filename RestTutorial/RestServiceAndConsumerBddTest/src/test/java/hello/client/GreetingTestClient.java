package hello.client;

import hello.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GreetingTestClient {
    private String greetingEndpointStringFormat;
    private String greetingWithNameEndpointStringFormat;
    private RestTemplate restTemplate;

    @Autowired
    public GreetingTestClient(@org.springframework.beans.factory.annotation.Value("${greetingEndpointStringFormat}") String greetingEndpointStringFormat,
                              @org.springframework.beans.factory.annotation.Value("${greetingWithNameEndpointStringFormat}") String greetingWithNameEndpointStringFormat,
                              @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.greetingEndpointStringFormat = greetingEndpointStringFormat;
        this.greetingWithNameEndpointStringFormat = greetingWithNameEndpointStringFormat;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Greeting> getGreeting() {
        return restTemplate.getForEntity(greetingEndpointStringFormat, Greeting.class);
    }

    public ResponseEntity<Greeting> getGreetingWithName(String name) {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "Harish");
        return restTemplate.getForEntity(greetingWithNameEndpointStringFormat, Greeting.class, queryParams);
    }
}
