package hello.client;

import hello.Quote;
import hello.helper.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuoteTestClient {
    private String quoteEndpointStringFormat;
    private TestHelper testHelper;
    private RestTemplate restTemplate;

    @Autowired
    public QuoteTestClient(
            @org.springframework.beans.factory.annotation.Value("${quoteEndpointStringFormat}") String quoteEndpointStringFormat,
            TestHelper testHelper,
            @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.quoteEndpointStringFormat = quoteEndpointStringFormat;
        this.testHelper = testHelper;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Quote> getQuote() {
        return restTemplate.getForEntity(getQuoteEndpointString(), Quote.class);
    }

    private String getQuoteEndpointString() {
        return String.format(quoteEndpointStringFormat, testHelper.getPort());
    }
}
