package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.client.ThirdPartyRestQuoteClientImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ThirdPartyRestQuoteClientImplIT extends BaseIntegration {

    @Autowired
    private ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;

    @Test
    public void getQuote_shouldGetQuote_fromThirdPartyStubService() {
        com.harishkannarao.rest.domain.Quote result = thirdPartyRestQuoteClientImpl.getQuote();
        assertEquals("success", result.getType());
        assertEquals("Working with Spring Boot is like pair-programming with the Spring developers.", result.getValue().getQuote());
        assertEquals(new Long(1L), result.getValue().getId());
    }

}
