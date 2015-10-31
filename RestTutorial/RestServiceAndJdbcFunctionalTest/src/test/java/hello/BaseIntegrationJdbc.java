package hello;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationJdbc.class})
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseIntegrationJdbc {
    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;
    @Autowired
    protected DbFixturesPopulator dbFixturesPopulator;

    @Before
    public void setup() {
        dbFixturesPopulator.initSchema();
        dbFixturesPopulator.resetData();
    }
}
