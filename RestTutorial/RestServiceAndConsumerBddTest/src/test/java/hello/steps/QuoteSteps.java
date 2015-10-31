package hello.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hello.Quote;
import hello.ThirdPartyRestQuoteClientImpl;
import hello.client.QuoteTestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class QuoteSteps extends BaseStep {
    private ResponseEntity<Quote> response;

    @Autowired
    private QuoteTestClient quoteTestClient;
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${thirdPartyQuoteEndpointStringFormat}")
    private String thirdPartyQuoteEndpointStringFormat;
    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    private ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;

    @Before
    public void setup() {
        thirdPartyRestQuoteClientImpl.setThirdPartyRestQuoteServiceUrl(String.format(thirdPartyQuoteEndpointStringFormat, port));
    }

    @Given("^QuoteEndpoint: I make a GET request$")
    public void callQuoteEndpoint() {
        response = quoteTestClient.getQuote();
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
