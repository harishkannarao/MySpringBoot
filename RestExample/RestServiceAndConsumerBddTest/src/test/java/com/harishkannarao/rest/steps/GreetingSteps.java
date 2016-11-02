package com.harishkannarao.rest.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import com.harishkannarao.rest.domain.Greeting;
import com.harishkannarao.rest.client.GreetingTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GreetingSteps extends BaseStep {
    private String name;
    private ResponseEntity<Greeting> response;
    @Autowired
    private GreetingTestClient greetingTestClient;

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
            response = greetingTestClient.getGreeting();
        } else {
            response = greetingTestClient.getGreetingWithName(name);
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
