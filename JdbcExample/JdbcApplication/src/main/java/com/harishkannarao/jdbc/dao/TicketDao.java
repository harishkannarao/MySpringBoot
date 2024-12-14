package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketDao {
	private final JdbcClient jdbcClient;

	@Autowired
	public TicketDao(@Qualifier("myJdbcClient") JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public void create(Ticket ticket) {
		jdbcClient.sql("""
				INSERT INTO tickets(id, status, customer_id, updated_time)
				 VALUES (:id, :status, :customerId, timezone('utc', now()))
				""")
			.paramSource(ticket)
			.update();
	}

	public List<Ticket> getAll() {
		return jdbcClient.sql("""
				SELECT * FROM tickets
				""")
			.query(Ticket.class)
			.list();
	}
}
