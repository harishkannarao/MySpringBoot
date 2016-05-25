package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class StatusControllerWithMockedThirdpartyPingClientIT extends BaseIntegration {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${statusEndpointUrl}")
    public String statusEndpointUrl;

    @Test
    public void getStatus_shouldReturnStatusOfThirdPartApps_capturedDuringStartup() {
        Status status = restTemplate.getForObject(statusEndpointUrl, Status.class);
        assertEquals("healthy", status.getThirdPartyStatus());
    }
}
