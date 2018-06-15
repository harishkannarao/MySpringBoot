package com.harishkannarao.jdbc_fixtures;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {JdbcApplicationDbFixtures.class},
        properties = {
                "application.run=false"
        }
)
public abstract class BaseIntegrationTestWithRestServiceAndJdbcDbFixturesApplication {
}
