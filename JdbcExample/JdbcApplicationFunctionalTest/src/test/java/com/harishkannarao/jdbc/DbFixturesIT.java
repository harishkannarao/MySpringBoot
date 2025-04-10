package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DbFixturesIT extends BaseIntegrationJdbc {
    @Autowired
    private DbFixturesPopulator dbFixturesPopulator;

    @Test
    public void getAllMenuEntries_shouldReturnAllMenuEntries_fromDatabase() {
        dbFixturesPopulator.insertData();
        dbFixturesPopulator.resetData();

        // should be able to rerun in the same sequence
        dbFixturesPopulator.insertData();
        dbFixturesPopulator.resetData();
    }
}
