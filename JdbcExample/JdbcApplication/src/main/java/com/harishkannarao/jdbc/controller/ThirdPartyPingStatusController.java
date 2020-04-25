package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.client.ThirdPartyPingRestClient;
import com.harishkannarao.jdbc.domain.ThirdPartyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyPingStatusController {

    private final ThirdPartyPingRestClient thirdPartyPingRestClient;

    @Autowired
    public ThirdPartyPingStatusController(ThirdPartyPingRestClient thirdPartyPingRestClient) {
        this.thirdPartyPingRestClient = thirdPartyPingRestClient;
    }

    @GetMapping("/third-party-ping-status")
    public ResponseEntity<ThirdPartyStatus> getQuote() {
        return ResponseEntity.ok()
                .body(thirdPartyPingRestClient.getPingStatus());
    }
}
