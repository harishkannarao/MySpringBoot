package hello;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.client.ThirdPartyPingRestClient;
import hello.client.ThirdPartyRestQuoteClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import({RestServiceAndConsumerApplication.class})
@PropertySources({
        @PropertySource("classpath:properties/${TEST_ENV:local}-test-config.properties")
})
public class TestConfigurationRestServiceAndConsumerApplication {
    @Bean
    @Qualifier("myThirdPartyRestQuoteClientImpl")
    @Primary
    public ThirdPartyRestQuoteClient overrideThirdPartyRestQuoteClientWithMockForTesting() {
        return mock(ThirdPartyRestQuoteClient.class);
    }

    @Bean
    @Qualifier("myThirdPartyPingRestClientImpl")
    @Primary
    public ThirdPartyPingRestClient overrideThirdPartyPingRestClientWithMockForTesting() {
        ThirdPartyPingRestClient mockedThirdPartyPingRestClient = mock(ThirdPartyPingRestClient.class);
        when(mockedThirdPartyPingRestClient.getPingStatus()).thenReturn("healthy");
        return mockedThirdPartyPingRestClient;
    }

    @Bean
    @Qualifier("myTestRestTemplate")
    public RestTemplate getMyTestRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new JsonHeaderInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    @Qualifier("myTestObjectMapper")
    public ObjectMapper getMyTestObjectMapper() {
        return new ObjectMapper();
    }

    @Bean(destroyMethod = "quit")
    public WebDriver getWebDriver() {
        DesiredCapabilities phantomjsCapabilities = DesiredCapabilities.phantomjs();
        String[] phantomJsArgs = {"--ignore-ssl-errors=true", "--ssl-protocol=any"};
        phantomjsCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
        WebDriver driver = new PhantomJSDriver(phantomjsCapabilities);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        return driver;
    }

}
