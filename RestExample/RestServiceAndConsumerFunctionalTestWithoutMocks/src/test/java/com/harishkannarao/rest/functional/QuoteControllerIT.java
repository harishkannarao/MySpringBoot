package com.harishkannarao.rest.functional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class QuoteControllerIT extends BaseIntegrationWithTestConfiguration {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${quoteEndpointUrl}")
    public String quoteEndpointUrl;

    @Test
    public void getQuote_shouldReturnQuoteDetails_fromThirdPartyRestService() {
        com.harishkannarao.rest.domain.Quote result = restTemplate.getForObject(quoteEndpointUrl, com.harishkannarao.rest.domain.Quote.class);
        assertEquals("success", result.getType());
        assertEquals("Working with Spring Boot is like pair-programming with the Spring developers.", result.getValue().getQuote());
        assertEquals(new Long(1L), result.getValue().getId());
    }
}
