package com.harishkannarao.rest.client;

import com.harishkannarao.rest.domain.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GreetingTestClient {
    private String greetingEndpointUrl;
    private String greetingWithNameEndpointUrl;
    private RestTemplate restTemplate;

    @Autowired
    public GreetingTestClient(@org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}") String greetingEndpointUrl,
                              @org.springframework.beans.factory.annotation.Value("${greetingWithNameEndpointUrl}") String greetingWithNameEndpointUrl,
                              @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.greetingEndpointUrl = greetingEndpointUrl;
        this.greetingWithNameEndpointUrl = greetingWithNameEndpointUrl;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Greeting> getGreeting() {
        return restTemplate.getForEntity(greetingEndpointUrl, Greeting.class);
    }

    public ResponseEntity<Greeting> getGreetingWithName(String name) {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("name", "Harish");
        return restTemplate.getForEntity(greetingWithNameEndpointUrl, Greeting.class, queryParams);
    }
}
