Feature: Quotes Feature

  Scenario: should return quote details from third party service
    Given QuoteEndpoint: I make a GET request
    Then QuoteEndpoint: I should see the type as "success"
    And QuoteEndpoint: I should see the quote as "Working with Spring Boot is like pair-programming with the Spring developers."
    And QuoteEndpoint: I should see the id as 1L