package com.harishkannarao.jdbc.client;

import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Component
public class ThirdPartyPingRestClient {
    private final RestClient restClient;
    private final String thirdPartyPingUrl;

    @Autowired
    public ThirdPartyPingRestClient(
            @Qualifier("myRestClient") RestClient restClient,
            @Value("${thirdparty.ping.url}") String thirdPartyPingRestUrl) {
        this.restClient = restClient;
        this.thirdPartyPingUrl = thirdPartyPingRestUrl;
    }

    public ThirdPartyStatus getPingStatus() {
        ResponseEntity<Void> exchange = restClient
					.get()
					.uri(thirdPartyPingUrl)
					.retrieve()
					.toBodilessEntity();
        ThirdPartyStatus status = new ThirdPartyStatus();
        status.setUrl(thirdPartyPingUrl);
        status.setStatus(exchange.getStatusCode().value());
        return status;
    }
}
