package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoteControllerIT extends RestServiceAndConsumerApplicationBaseIntegrationTest {
    public static final String quoteEndpointStringFormat = "http://localhost:%s/quote";

    @Configuration
    public static class TestConfiguration {
        @Bean
        @Qualifier("myThirdPartyRestQuoteClientImpl")
        @Primary
        public ThirdPartyRestQuoteClient thirdPartyRestQuoteClient() {
            return mock(ThirdPartyRestQuoteClient.class);
        }
    }

    @Autowired
    ThirdPartyRestQuoteClient mockThirdPartyRestQuoteClient;

    private String getQuoteEndpointString() {
        return String.format(quoteEndpointStringFormat, port);
    }

    @Test
    public void getQuote_shouldReturnQuoteDetails_fromThirdPartyRestService() {
        Quote expectedQuoteFromThirdPartyService = new Quote();
        expectedQuoteFromThirdPartyService.setType("success");
        expectedQuoteFromThirdPartyService.setValue(new Value());
        expectedQuoteFromThirdPartyService.getValue().setId(1L);
        expectedQuoteFromThirdPartyService.getValue().setQuote("Working with Spring Boot is like pair-programming with the Spring developers.");
        when(mockThirdPartyRestQuoteClient.getQuote()).thenReturn(expectedQuoteFromThirdPartyService);

        Quote result = restTemplate.getForObject(getQuoteEndpointString(), Quote.class);
        assertEquals("success", result.getType());
        assertEquals("Working with Spring Boot is like pair-programming with the Spring developers.", result.getValue().getQuote());
        assertEquals(new Long(1L), result.getValue().getId());
    }
}
