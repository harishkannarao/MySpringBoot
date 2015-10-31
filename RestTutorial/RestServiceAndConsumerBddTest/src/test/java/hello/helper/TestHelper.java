package hello.helper;

import hello.ThirdPartyRestQuoteClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {
    private String thirdPartyQuoteEndpointStringFormat;
    private Environment environment;
    private ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl;

    @Autowired
    public TestHelper(
            @org.springframework.beans.factory.annotation.Value("${thirdPartyQuoteEndpointStringFormat}")
            String thirdPartyQuoteEndpointStringFormat,
            @Qualifier("myThirdPartyRestQuoteClientImpl")
            ThirdPartyRestQuoteClientImpl thirdPartyRestQuoteClientImpl,
            Environment environment) {
        this.thirdPartyQuoteEndpointStringFormat = thirdPartyQuoteEndpointStringFormat;
        this.thirdPartyRestQuoteClientImpl = thirdPartyRestQuoteClientImpl;
        this.environment = environment;
    }

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

    public void configureThirdPartyRestQuoteClientUrl() {
        thirdPartyRestQuoteClientImpl.setThirdPartyRestQuoteServiceUrl(String.format(thirdPartyQuoteEndpointStringFormat, getPort()));
    }

}
