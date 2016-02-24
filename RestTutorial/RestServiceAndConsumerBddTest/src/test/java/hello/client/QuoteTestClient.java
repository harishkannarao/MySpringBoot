package hello.client;

import hello.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuoteTestClient {
    private String quoteEndpointStringFormat;
    private RestTemplate restTemplate;

    @Autowired
    public QuoteTestClient(
            @org.springframework.beans.factory.annotation.Value("${quoteEndpointStringFormat}") String quoteEndpointStringFormat,
            @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.quoteEndpointStringFormat = quoteEndpointStringFormat;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Quote> getQuote() {
        return restTemplate.getForEntity(quoteEndpointStringFormat, Quote.class);
    }

}
