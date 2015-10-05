package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("myThirdPartyRestQuoteClientImpl")
public class ThirdPartyRestQuoteClientImpl implements ThirdPartyRestQuoteClient {
    private RestTemplate restTemplate;

    @Autowired
    public ThirdPartyRestQuoteClientImpl(@Qualifier("myRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Quote getQuote() {
        return restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
    }
}
