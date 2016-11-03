package com.harishkannarao.rest.stub.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyRestPingController {

    @RequestMapping("/thirdparty/ping")
    public String getPingStatus() {
        return "some status from stubbed third party";
    }
}
