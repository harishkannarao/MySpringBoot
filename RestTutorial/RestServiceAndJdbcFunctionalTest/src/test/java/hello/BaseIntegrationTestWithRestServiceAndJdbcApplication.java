package hello;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Sql(
        scripts = "/dbscripts/create-test-schema.sql",
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
@Sql(
        scripts = "/dbscripts/create-test-data.sql",
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RestServiceAndJdbcApplication.class})
@WebIntegrationTest({
        "server.port=0",
        "management.port=0"
})
public abstract class BaseIntegrationTestWithRestServiceAndJdbcApplication {
    @Autowired
    protected EmbeddedWebApplicationContext server;
    @org.springframework.beans.factory.annotation.Value("${local.server.port}")
    protected int port;
    protected RestTemplate restTemplate = getRestTemplate();
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = webAppContextSetup(wac).build();
    }

    protected RestTemplate getRestTemplate() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new JsonHeaderInterceptor());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
