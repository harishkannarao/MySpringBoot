package hello;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;

public class ThirdPartyRestQuoteClientImplIT extends BaseIntegrationTestWithThirdPartyRestServiceStubApplication {

    public static final String thirdPartyQuoteEndpointStringFormat = "http://localhost:%s/thirdparty/quote";

    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    private ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;

    private String getThirdPartyQuoteEndpointString() {
        return String.format(thirdPartyQuoteEndpointStringFormat, port);
    }

    @Before
    public void setUp() {
        thirdPartyRestQuoteClientImpl.setThirdPartyRestQuoteServiceUrl(getThirdPartyQuoteEndpointString());
    }

    @Test
    public void getQuote_shouldGetQuote_fromThirdPartyStubService() {
        Quote result = thirdPartyRestQuoteClientImpl.getQuote();
        assertEquals("success", result.getType());
        assertEquals("Working with Spring Boot is like pair-programming with the Spring developers.", result.getValue().getQuote());
        assertEquals(new Long(1L), result.getValue().getId());
    }

}
