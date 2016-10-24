package hello.controller;

import hello.client.ThirdPartyPingRestClient;
import hello.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final ThirdPartyPingRestClient thirdPartyPingRestClient;
    private final Status result;

    @Autowired
    public StatusController(@Qualifier("myThirdPartyPingRestClientImpl")ThirdPartyPingRestClient thirdPartyPingRestClient) {
        this.thirdPartyPingRestClient = thirdPartyPingRestClient;
        result = new Status();
        result.setThirdPartyStatus(thirdPartyPingRestClient.getPingStatus());
    }

    @RequestMapping("/status")
    public Status getQuote() {
        return result;
    }
}