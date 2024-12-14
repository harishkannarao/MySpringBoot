package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

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

	public long getAvailableTickets() {
		return jdbcClient.sql("""
				SELECT COUNT(id) FROM tickets
				WHERE status='AVAILABLE'
				""")
			.query(Long.class)
			.single();
	}

	@Transactional
	public Optional<UUID> reserveTicket(UUID customerId) {
		Optional<UUID> ticketId = jdbcClient.sql("""
				SELECT id FROM tickets
				WHERE status='AVAILABLE'
				LIMIT 1
				FOR UPDATE
				""")
			.query(UUID.class)
			.optional();
		return ticketId.flatMap((UUID tId) -> {
				int rowsUpdated = jdbcClient.sql("""
						UPDATE tickets
						 SET customer_id=:customerId,
						 status='BOOKED',
						 updated_time=timezone('utc', now())
						 WHERE id=:id AND status='AVAILABLE'
						""")
					.param("customerId", customerId)
					.param("id", tId)
					.update();
				return rowsUpdated == 1 ? Optional.of(tId) : Optional.empty();
			}
		);
	}
}
