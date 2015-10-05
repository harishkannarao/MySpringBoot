package hello;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("myThirdPartyRestQuoteClientImpl")
public class ThirdPartyRestQuoteClientImpl implements ThirdPartyRestQuoteClient {
    private RestTemplate restTemplate = new RestTemplate();

    public Quote getQuote() {
        return restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
    }
}
