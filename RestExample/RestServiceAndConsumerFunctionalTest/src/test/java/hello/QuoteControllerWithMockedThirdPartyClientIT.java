package hello;

import hello.client.ThirdPartyRestQuoteClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class QuoteControllerWithMockedThirdPartyClientIT extends BaseIntegration {
    @org.springframework.beans.factory.annotation.Value("${quoteEndpointUrl}")
    public String quoteEndpointUrl;

    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    private ThirdPartyRestQuoteClient mockThirdPartyRestQuoteClient;

    @Test
    public void getQuote_shouldReturnQuoteDetails_fromThirdPartyRestService() {
        hello.domain.Quote expectedQuoteFromThirdPartyService = hello.domain.QuoteBuilder.newBuilder().setType("some type")
                .setValue(hello.domain.ValueBuilder.newBuilder().setId(2L).setQuote("some quote"))
                .build();
        when(mockThirdPartyRestQuoteClient.getQuote()).thenReturn(expectedQuoteFromThirdPartyService);

        hello.domain.Quote result = restTemplate.getForObject(quoteEndpointUrl, hello.domain.Quote.class);
        assertEquals("some type", result.getType());
        assertEquals("some quote", result.getValue().getQuote());
        assertEquals(new Long(2L), result.getValue().getId());
    }
}
