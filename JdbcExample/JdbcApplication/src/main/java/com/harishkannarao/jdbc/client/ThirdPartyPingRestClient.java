package com.harishkannarao.jdbc.client;

import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ThirdPartyPingRestClient {
    private final RestTemplate restTemplate;
    private final String thirdPartyPingUrl;

    @Autowired
    public ThirdPartyPingRestClient(
            @Qualifier("myRestTemplate") RestTemplate restTemplate,
            @Value("${thirdparty.ping.url}") String thirdPartyPingRestUrl) {
        this.restTemplate = restTemplate;
        this.thirdPartyPingUrl = thirdPartyPingRestUrl;
    }

    public ThirdPartyStatus getPingStatus() {
        ResponseEntity<Void> exchange = restTemplate.exchange(thirdPartyPingUrl, HttpMethod.GET, null, Void.class);
        ThirdPartyStatus status = new ThirdPartyStatus();
        status.setUrl(thirdPartyPingUrl);
        status.setStatus(exchange.getStatusCodeValue());
        return status;
    }
}
