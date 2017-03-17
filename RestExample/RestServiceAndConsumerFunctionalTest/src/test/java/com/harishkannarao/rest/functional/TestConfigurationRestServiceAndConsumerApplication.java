package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.rest.client.ThirdPartyPingRestClient;
import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@PropertySources({
        @PropertySource("classpath:properties/${test.env:local}-test-config.properties")
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
    public TestRestTemplate getMyTestRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .additionalInterceptors(new JsonHeaderInterceptor());
        return new TestRestTemplate(builder);
    }

    @Bean
    @Qualifier("myTestRestTemplateForHtml")
    public TestRestTemplate getMyTestRestTemplateForHtml() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .additionalInterceptors(new HtmlAcceptHeaderInterceptor());
        return new TestRestTemplate(builder);
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
