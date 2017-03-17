package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.Status;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatusControllerWithMockedThirdpartyPingClientIT extends BaseIntegration {
    @org.springframework.beans.factory.annotation.Value("${statusEndpointUrl}")
    public String statusEndpointUrl;

    @Test
    public void getStatus_shouldReturnStatusOfThirdPartApps_capturedDuringStartup() {
        Status status = testRestTemplate.getForObject(statusEndpointUrl, Status.class);
        assertEquals("healthy", status.getThirdPartyStatus());
    }
}
