package hello;

import hello.client.ThirdPartyPingRestClientImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;

public class ThirdPartyPingRestClientImplIT extends BaseIntegrationWithThirdPartyStubApplication {

    @Autowired
    @Qualifier("myThirdPartyPingRestClientImpl")
    private ThirdPartyPingRestClientImpl thirdPartyPingRestClientImpl;

    @Test
    public void getStatus_shouldGetPingStatus_fromThirdPartyStubService() {
        String result = thirdPartyPingRestClientImpl.getPingStatus();
        assertEquals("some status from stubbed third party", result);
    }

}
