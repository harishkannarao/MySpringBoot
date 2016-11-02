package com.harishkannarao.rest.controller;

import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    private ThirdPartyRestQuoteClient thirdPartyRestQuoteClient;

    @Autowired
    public QuoteController(@Qualifier("myThirdPartyRestQuoteClientImpl")ThirdPartyRestQuoteClient thirdPartyRestQuoteClient) {
        this.thirdPartyRestQuoteClient = thirdPartyRestQuoteClient;
    }

    @RequestMapping("/quote")
    public Quote getQuote() {
        return thirdPartyRestQuoteClient.getQuote();
    }
}
