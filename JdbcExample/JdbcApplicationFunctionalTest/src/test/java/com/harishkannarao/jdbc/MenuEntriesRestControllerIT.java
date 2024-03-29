package com.harishkannarao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuEntriesRestControllerIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${allMenuEntriesEndpointUrl}")
	public String allMenuEntriesEndpointUrl;

	@Test
	public void getAllMenuEntries_shouldReturnAllMenuEntries_fromDatabase() {
		String[] menuEntriesArray = restClient
			.get()
			.uri(allMenuEntriesEndpointUrl)
			.retrieve()
			.body(String[].class);
		List<String> menuEntries = Arrays.asList(requireNonNull(menuEntriesArray));
		assertEquals(3, menuEntries.size());
	}
}
