package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RestServiceAndJdbcDbFixturesApplication.class})
@IntegrationTest
public abstract class BaseIntegrationTestWithRestServiceAndJdbcDbFixturesApplication {
}
