package com.harishkannarao.rest.client;

import com.harishkannarao.rest.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuoteTestClient {
    private String quoteEndpointUrl;
    private RestTemplate restTemplate;

    @Autowired
    public QuoteTestClient(
            @org.springframework.beans.factory.annotation.Value("${quoteEndpointUrl}") String quoteEndpointUrl,
            @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.quoteEndpointUrl = quoteEndpointUrl;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Quote> getQuote() {
        return restTemplate.getForEntity(quoteEndpointUrl, Quote.class);
    }

}
