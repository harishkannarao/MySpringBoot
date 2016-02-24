package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;

public class ThirdPartyRestQuoteClientImplIT extends BaseIntegrationWithThirdPartyStubApplication {

    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    private ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;

    @Test
    public void getQuote_shouldGetQuote_fromThirdPartyStubService() {
        Quote result = thirdPartyRestQuoteClientImpl.getQuote();
        assertEquals("success", result.getType());
        assertEquals("Working with Spring Boot is like pair-programming with the Spring developers.", result.getValue().getQuote());
        assertEquals(new Long(1L), result.getValue().getId());
    }

}
