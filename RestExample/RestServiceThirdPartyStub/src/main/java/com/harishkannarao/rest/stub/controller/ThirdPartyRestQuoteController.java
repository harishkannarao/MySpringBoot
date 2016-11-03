package com.harishkannarao.rest.stub.controller;

import com.harishkannarao.rest.stub.domain.Quote;
import com.harishkannarao.rest.stub.domain.QuoteBuilder;
import com.harishkannarao.rest.stub.domain.ValueBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyRestQuoteController {

    @RequestMapping("/thirdparty/quote")
    public Quote getQuote() {
        return QuoteBuilder.newBuilder()
                .setType("success")
                .setValue(ValueBuilder.newBuilder()
                        .setQuote("Working with Spring Boot is like pair-programming with the Spring developers.")
                        .setId(1L)
                )
                .build();
    }
}
