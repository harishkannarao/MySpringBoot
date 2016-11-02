package com.harishkannarao.rest.steps;

import cucumber.api.java.en.Given;
import com.harishkannarao.rest.domain.Quote;
import com.harishkannarao.rest.domain.QuoteBuilder;
import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.domain.ValueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class ThirdPartyRestQuoteClientSteps extends BaseStep {
    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    private ThirdPartyRestQuoteClient mockThirdPartyRestQuoteClient;

    @Given("^ThirdPartyRestQuoteClient: I reset the mock$")
    public void resetMock() {
        reset(mockThirdPartyRestQuoteClient);
    }

    @Given("^ThirdPartyRestQuoteClient: I setup third party rest quote client to return a quote$")
    public void setupThirdPartyRestQuoteClient() {
        Quote expectedQuoteFromThirdPartyService = QuoteBuilder.newBuilder().setType("some type")
                .setValue(ValueBuilder.newBuilder().setId(2L).setQuote("some quote"))
                .build();
        when(mockThirdPartyRestQuoteClient.getQuote()).thenReturn(expectedQuoteFromThirdPartyService);
    }
}
