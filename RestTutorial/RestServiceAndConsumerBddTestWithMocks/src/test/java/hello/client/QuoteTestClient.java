package hello.client;

import hello.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuoteTestClient {
    private String quoteEndpointStringFormat;
    private Environment environment;
    private RestTemplate restTemplate;

    @Autowired
    public QuoteTestClient(
            @org.springframework.beans.factory.annotation.Value("${quoteEndpointStringFormat}") String quoteEndpointStringFormat,
            Environment environment,
            @Qualifier("myTestRestTemplate") RestTemplate restTemplate) {
        this.quoteEndpointStringFormat = quoteEndpointStringFormat;
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Quote> getQuote() {
        return restTemplate.getForEntity(getQuoteEndpointString(), Quote.class);
    }

    private String getPort() {
        return environment.getProperty("local.server.port");
    }

    private String getQuoteEndpointString() {
        return String.format(quoteEndpointStringFormat, getPort());
    }
}
