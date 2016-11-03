package com.harishkannarao.jdbc_fixtures;

import com.harishkannarao.jdbc.fixtures.DbFixturesPopulator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DbFixturesIT extends BaseIntegrationTestWithRestServiceAndJdbcDbFixturesApplication {

    @Autowired
    private DbFixturesPopulator dbFixturesPopulator;

    @Test
    public void shouldBeAbleToResetFixturesConsitently() {
        dbFixturesPopulator.createSchema();
        dbFixturesPopulator.insertData();
        dbFixturesPopulator.resetData();

        // should be able to re run in the same sequence
        dbFixturesPopulator.createSchema();
        dbFixturesPopulator.insertData();
        dbFixturesPopulator.resetData();
    }
}
