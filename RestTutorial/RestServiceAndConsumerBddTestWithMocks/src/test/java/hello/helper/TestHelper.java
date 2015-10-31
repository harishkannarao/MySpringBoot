package hello.helper;

import hello.ThirdPartyRestQuoteClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {
    private Environment environment;

    @Autowired
    public TestHelper(
            Environment environment) {
        this.environment = environment;
    }

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

}
