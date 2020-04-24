package com.harishkannarao.jdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("myMenuEntriesDao")
public class MenuEntriesDao {

    private List<String> menuEntries;

    @Autowired
    public MenuEntriesDao(JdbcTemplate jdbcTemplate) {
        RowMapper<String> rowMapper = (rs, rowNum) -> rs.getString("entry");
        menuEntries = jdbcTemplate.query(
                "SELECT entry FROM menu_entries",
                rowMapper
        );
    }

    public List<String> getMenuEntries() {
        return menuEntries;
    }
}
