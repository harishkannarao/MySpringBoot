package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteController {

    private ThirdPartyRestQuoteClient thirdPartyRestQuoteClient;

    public void setThirdPartyRestQuoteClient(ThirdPartyRestQuoteClient thirdPartyRestQuoteClient) {
        this.thirdPartyRestQuoteClient = thirdPartyRestQuoteClient;
    }

    @Autowired
    public QuoteController(@Qualifier("myThirdPartyRestQuoteClientImpl")ThirdPartyRestQuoteClient thirdPartyRestQuoteClient) {
        this.thirdPartyRestQuoteClient = thirdPartyRestQuoteClient;
    }

    @RequestMapping("/quote")
    public Quote getQuote() {
        return thirdPartyRestQuoteClient.getQuote();
    }
}
