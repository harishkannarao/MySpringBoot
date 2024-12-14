package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

@TestComponent
public class TicketTestSupportDao {
	private final JdbcClient jdbcClient;

	@Autowired
	public TicketTestSupportDao(@Qualifier("myJdbcClient") JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public List<Ticket> getAll() {
		return jdbcClient.sql("""
				SELECT * FROM tickets
				""")
			.query(Ticket.class)
			.list();
	}

	public void deleteAll() {
		jdbcClient.sql("""
				DELETE FROM tickets
				""")
			.update();
	}
}
