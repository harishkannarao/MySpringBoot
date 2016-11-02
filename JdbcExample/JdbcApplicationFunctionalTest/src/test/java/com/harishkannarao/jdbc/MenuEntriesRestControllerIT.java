package com.harishkannarao.jdbc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MenuEntriesRestControllerIT extends BaseIntegrationJdbc {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${allMenuEntriesEndpointUrl}")
    public String allMenuEntriesEndpointUrl;

    @Test
    public void getAllMenuEntries_shouldReturnAllMenuEntries_fromDatabase() {
        String[] menuEntriesArray = restTemplate.getForObject(allMenuEntriesEndpointUrl, String[].class);
        List<String> menuEntries = Arrays.asList(menuEntriesArray);
        assertEquals(3, menuEntries.size());
    }
}
