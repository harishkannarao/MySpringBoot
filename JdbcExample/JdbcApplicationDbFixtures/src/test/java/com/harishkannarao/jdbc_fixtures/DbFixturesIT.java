package com.harishkannarao.jdbc_fixtures;

import com.harishkannarao.jdbc.fixtures.DbFixturesPopulator;
import org.junit.Test;

public class DbFixturesIT extends BaseIntegrationTestWithRestServiceAndJdbcDbFixturesApplication {

    @Test
    public void shouldBeAbleToResetFixturesConsitently() {
        runSpringApplication();

        DbFixturesPopulator dbFixturesPopulator = application.getBean(DbFixturesPopulator.class);

        dbFixturesPopulator.createSchema();
        dbFixturesPopulator.insertData();
        dbFixturesPopulator.resetData();

        // should be able to re run in the same sequence
        dbFixturesPopulator.createSchema();
        dbFixturesPopulator.insertData();
        dbFixturesPopulator.resetData();
    }
}
