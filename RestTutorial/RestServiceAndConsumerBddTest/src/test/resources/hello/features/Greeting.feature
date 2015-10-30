Feature: Greetings Feature

  Scenario: should return default greeting when name is not specified
    Given GreetingEndpoint: I do not set the name
    And GreetingEndpoint: I make a GET request
    Then GreetingEndpoint: I should see some Id
    And GreetingEndpoint: I should see the message as "Hello, World!"

  Scenario: should return custom greeting when name is specified
    Given GreetingEndpoint: I set the name as "Harish"
    And GreetingEndpoint: I make a GET request
    Then GreetingEndpoint: I should see some Id
    And GreetingEndpoint: I should see the message as "Hello, Harish!"