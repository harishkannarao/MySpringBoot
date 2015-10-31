package hello;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RestServiceAndConsumerApplication.class, ThirdPartyRestServiceStubApplication.class})
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseIntegrationTestWithRestServiceAndConsumerAndStubbedServiceApplications {
    public static final String thirdPartyQuoteEndpointStringFormat = "http://localhost:%s/thirdparty/quote";

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
    @Autowired
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    protected ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;
    protected RestTemplate restTemplate = getRestTemplate();

    @Before
    public void setup() {
        thirdPartyRestQuoteClientImpl.setThirdPartyRestQuoteServiceUrl(getThirdPartyQuoteEndpointString());
    }

    private RestTemplate getRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new JsonHeaderInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    private String getThirdPartyQuoteEndpointString() {
        return String.format(thirdPartyQuoteEndpointStringFormat, port);
    }
}

