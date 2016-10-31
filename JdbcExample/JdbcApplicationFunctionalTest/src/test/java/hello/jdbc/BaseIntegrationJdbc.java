package hello.jdbc;

import hello.jdbc.fixtures.DbFixturesPopulator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {TestConfigurationJdbcApplication.class},
        webEnvironment = DEFINED_PORT,
        properties = {
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
