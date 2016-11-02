package com.harishkannarao.rest.client;

import com.harishkannarao.rest.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("myThirdPartyRestQuoteClientImpl")
public class ThirdPartyRestQuoteClientImpl implements ThirdPartyRestQuoteClient {
    private final RestTemplate restTemplate;
    private final String thirdPartyRestQuoteServiceUrl;

    @Autowired
    public ThirdPartyRestQuoteClientImpl(
            @Qualifier("myRestTemplate") RestTemplate restTemplate,
            @org.springframework.beans.factory.annotation.Value("${quoteService.url}")String thirdPartyRestQuoteServiceUrl) {
        this.restTemplate = restTemplate;
        this.thirdPartyRestQuoteServiceUrl = thirdPartyRestQuoteServiceUrl;
    }

    public Quote getQuote() {
        return restTemplate.getForObject(thirdPartyRestQuoteServiceUrl, Quote.class);
    }
}
