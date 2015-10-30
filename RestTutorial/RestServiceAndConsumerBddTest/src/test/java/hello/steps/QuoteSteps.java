package hello.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hello.Quote;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class QuoteSteps extends BaseStep {
    private ResponseEntity<Quote> response;

    public static final String quoteEndpointStringFormat = "http://localhost:%s/quote";
    public static final String thirdPartyQuoteEndpointStringFormat = "http://localhost:%s/thirdparty/quote";

    @Before
    public void setup() {
        thirdPartyRestQuoteClientImpl.setThirdPartyRestQuoteServiceUrl(String.format(thirdPartyQuoteEndpointStringFormat, port));
    }

    private String getQuoteEndpointString() {
        return String.format(quoteEndpointStringFormat, port);
    }

    @Given("^QuoteEndpoint: I make a GET request$")
    public void callQuoteEndpoint() {
        response = restTemplate.getForEntity(getQuoteEndpointString(), Quote.class);
    }

    @Then("^QuoteEndpoint: I should see the type as \"(.*)\"$")
    public void assertType(String exptectedType) {
        assertEquals(exptectedType, response.getBody().getType());
    }
    @And("^QuoteEndpoint: I should see the quote as \"(.*)\"$")
    public void assertQuote(String expectedQuote) {
        assertEquals(expectedQuote, response.getBody().getValue().getQuote());
    }

    @And("^QuoteEndpoint: I should see the id as (.*)$")
    public void assertId(Long expectedId) {
        assertEquals(expectedId, response.getBody().getValue().getId());
    }

}
