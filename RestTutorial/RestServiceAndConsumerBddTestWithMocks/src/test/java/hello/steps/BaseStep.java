package hello.steps;

import hello.QuoteController;
import hello.RestServiceAndConsumerApplication;
import hello.ThirdPartyRestQuoteClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {RestServiceAndConsumerApplication.class, RestServiceAndConsumerApplication.class}, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseStep {

    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
    @Autowired
    protected QuoteController quoteController;
    protected RestTemplate restTemplate = getRestTemplate();

    private RestTemplate getRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new JsonHeaderInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}

