package hello.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import hello.Quote;
import hello.QuoteBuilder;
import hello.ThirdPartyRestQuoteClient;
import hello.ValueBuilder;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoteWithMockedThirdPartySteps extends BaseStep {
    private ResponseEntity<Quote> response;
    private ThirdPartyRestQuoteClient mockedThirdPartyRestQuoteClient;
    public static final String quoteEndpointStringFormat = "http://localhost:%s/quote";

    @Before
    public void setup() {
        mockedThirdPartyRestQuoteClient = mock(ThirdPartyRestQuoteClient.class);
        quoteController.setThirdPartyRestQuoteClient(mockedThirdPartyRestQuoteClient);
    }

    private String getQuoteEndpointString() {
        return String.format(quoteEndpointStringFormat, port);
    }

    @Given("^QuoteEndpointWithMockedThirdParty: I setup third party rest quote client to return a quote$")
    public void setupThirdPartyRestQuoteClient() {
        Quote expectedQuoteFromThirdPartyService = QuoteBuilder.newBuilder().setType("some type")
                .setValue(ValueBuilder.newBuilder().setId(2L).setQuote("some quote"))
                .build();
        when(mockedThirdPartyRestQuoteClient.getQuote()).thenReturn(expectedQuoteFromThirdPartyService);
    }

    @Given("^QuoteEndpointWithMockedThirdParty: I make a GET request$")
    public void callQuoteEndpoint() {
        response = restTemplate.getForEntity(getQuoteEndpointString(), Quote.class);
    }

    @Then("^QuoteEndpointWithMockedThirdParty: I should see the type as \"(.*)\"$")
    public void assertType(String exptectedType) {
        assertEquals(exptectedType, response.getBody().getType());
    }

    @And("^QuoteEndpointWithMockedThirdParty: I should see the quote as \"(.*)\"$")
    public void assertQuote(String expectedQuote) {
        assertEquals(expectedQuote, response.getBody().getValue().getQuote());
    }

    @And("^QuoteEndpointWithMockedThirdParty: I should see the id as (.*)$")
    public void assertId(Long expectedId) {
        assertEquals(expectedId, response.getBody().getValue().getId());
    }
}
