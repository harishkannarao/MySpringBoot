package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.client.ThirdPartyPingRestClientImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ThirdPartyPingRestClientImplIT extends BaseIntegration {

    @Autowired
    private ThirdPartyPingRestClientImpl thirdPartyPingRestClientImpl;

    @Test
    public void getStatus_shouldGetPingStatus_fromThirdPartyStubService() {
        String result = thirdPartyPingRestClientImpl.getPingStatus();
        assertEquals("some status from stubbed third party", result);
    }

}
