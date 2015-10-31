package hello;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuoteControllerIT extends BaseIntegrationWithTestConfiguration {
    public static final String quoteEndpointStringFormat = "http://localhost:%s/quote";

    private String getQuoteEndpointString() {
        return String.format(quoteEndpointStringFormat, port);
    }

    @Test
    public void getQuote_shouldReturnQuoteDetails_fromThirdPartyRestService() {
        Quote result = restTemplate.getForObject(getQuoteEndpointString(), Quote.class);
        assertEquals("success", result.getType());
        assertEquals("Working with Spring Boot is like pair-programming with the Spring developers.", result.getValue().getQuote());
        assertEquals(new Long(1L), result.getValue().getId());
    }
}
