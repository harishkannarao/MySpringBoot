package com.harishkannarao.jdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("myMenuEntriesDao")
public class MenuEntriesDao {

	private final List<String> menuEntries;

	@Autowired
	public MenuEntriesDao(@Qualifier("myJdbcClient") JdbcClient jdbcClient) {
		menuEntries = jdbcClient.sql("SELECT entry FROM menu_entries")
			.query(String.class).list();
	}

	public List<String> getMenuEntries() {
		return menuEntries;
	}
}
