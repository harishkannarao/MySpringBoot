package hello;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfiguration.class})
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseIntegrationWithTestConfiguration {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${thirdPartyQuoteEndpointStringFormat}")
    public String thirdPartyQuoteEndpointStringFormat;

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    protected ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;

    @Before
    public void setup() {
        thirdPartyRestQuoteClientImpl.setThirdPartyRestQuoteServiceUrl(getThirdPartyQuoteEndpointString());
    }

    private String getThirdPartyQuoteEndpointString() {
        return String.format(thirdPartyQuoteEndpointStringFormat, port);
    }
}

