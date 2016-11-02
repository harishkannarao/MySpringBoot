Feature: Quotes with mocked third party service Feature

  Background:
    Given ThirdPartyRestQuoteClient: I reset the mock

  Scenario: should return quote details from mocked third party service
    Given ThirdPartyRestQuoteClient: I setup third party rest quote client to return a quote
    And QuoteEndpointWithMockedThirdParty: I make a GET request
    Then QuoteEndpointWithMockedThirdParty: I should see the type as "some type"
    And QuoteEndpointWithMockedThirdParty: I should see the quote as "some quote"
    And QuoteEndpointWithMockedThirdParty: I should see the id as 2L