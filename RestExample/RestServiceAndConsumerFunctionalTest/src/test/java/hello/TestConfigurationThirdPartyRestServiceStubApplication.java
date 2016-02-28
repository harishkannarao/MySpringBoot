package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Import({RestServiceThirdPartyStubApplication.class})
@PropertySources({
        @PropertySource("classpath:properties/${TEST_ENV:local}-test-config.properties")
})
public class TestConfigurationThirdPartyRestServiceStubApplication {
}
