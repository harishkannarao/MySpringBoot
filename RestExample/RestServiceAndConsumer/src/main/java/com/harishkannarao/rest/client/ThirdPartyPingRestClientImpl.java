package com.harishkannarao.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("myThirdPartyPingRestClientImpl")
public class ThirdPartyPingRestClientImpl implements ThirdPartyPingRestClient {
    private final RestTemplate restTemplate;
    private final String thirdPartyPingUrl;

    @Autowired
    public ThirdPartyPingRestClientImpl(
            @Qualifier("myRestTemplate") RestTemplate restTemplate,
            @org.springframework.beans.factory.annotation.Value("${thirdparty.ping.url}")String thirdPartyPingRestUrl) {
        this.restTemplate = restTemplate;
        this.thirdPartyPingUrl = thirdPartyPingRestUrl;
    }

    @Override
    public String getPingStatus() {
        return restTemplate.getForObject(thirdPartyPingUrl, String.class);
    }
}
