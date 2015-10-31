package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ThirdPartyRestServiceStubApplication.class})
public class TestConfigurationThirdPartyRestServiceStubApplication {
}
