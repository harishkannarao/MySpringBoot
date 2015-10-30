package hello.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hello.Greeting;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GreetingSteps extends BaseStep {
    private String name;
    private ResponseEntity<Greeting> response;

    public static final String greetingEndpointStringFormat = "http://localhost:%s/greeting/get";
    public static final String greetingWithNameEndpointStringFormat = greetingEndpointStringFormat + "?name={name}";

    private String getGreetingEndpointString() {
        return String.format(greetingEndpointStringFormat, port);
    }

    private String getGreetingWithNameEndpointString() {
        return String.format(greetingWithNameEndpointStringFormat, port);
    }

    @Given("^GreetingEndpoint: I do not set the name$")
    public void setNameAsNull() {
        name = null;
    }

    @Given("^GreetingEndpoint: I set the name as \"(.*)\"$")
    public void setName(String inputName){
        name = inputName;
    }

    @And("^GreetingEndpoint: I make a GET request$")
    public void makeGetRequest() {
        if (name == null) {
            response = restTemplate.getForEntity(getGreetingEndpointString(), Greeting.class);
        } else {
            Map<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("name", "Harish");
            response = restTemplate.getForEntity(getGreetingWithNameEndpointString(), Greeting.class, queryParams);
        }
    }

    @Then("^GreetingEndpoint: I should see some Id$")
    public void assertId() {
        assertNotNull(response.getBody().getId());
    }

    @And("^GreetingEndpoint: I should see the message as \"(.*)\"$")
    public void assertGreeting(String expectedGreeting) {
        assertEquals(expectedGreeting, response.getBody().getContent());
    }

}
