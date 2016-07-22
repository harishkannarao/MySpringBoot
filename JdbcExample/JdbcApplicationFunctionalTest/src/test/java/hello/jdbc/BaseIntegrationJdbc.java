package hello.jdbc;

import hello.jdbc.fixtures.DbFixturesPopulator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestConfigurationJdbcApplication.class})
@WebIntegrationTest({
        "server.port=8180",
        "management.port=8181",
        "spring.datasource.schema=classpath:/dbscripts/create-test-schema.sql",
        "spring.datasource.data=classpath:/dbscripts/delete-test-data.sql,classpath:/dbscripts/create-test-data.sql"
})
public abstract class BaseIntegrationJdbc {
    @Autowired
    @Qualifier("myTestRestTemplate")
    protected RestTemplate restTemplate;
    @Autowired
    protected DbFixturesPopulator dbFixturesPopulator;

    @Before
    public void setup() {
        dbFixturesPopulator.resetData();
    }
}
