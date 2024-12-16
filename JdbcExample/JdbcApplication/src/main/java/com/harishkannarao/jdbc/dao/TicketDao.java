package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TicketDao {
	private final JdbcClient jdbcClient;
	private final int cleanUpBatchSize;
	private final Duration expiry;

	@Autowired
	public TicketDao(
		@Qualifier("myJdbcClient") JdbcClient jdbcClient,
		@Value("${app.clean-up-reservation.batch-size}") int cleanUpBatchSize,
		@Value("${app.clean-up-reservation.expiry-duration}") Duration expiry) {
		this.jdbcClient = jdbcClient;
		this.cleanUpBatchSize = cleanUpBatchSize;
		this.expiry = expiry;
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
		Optional<UUID> selectedTicketId = jdbcClient.sql("""
				SELECT id FROM tickets
				WHERE status='AVAILABLE'
				LIMIT 1
				FOR UPDATE
				""")
			.query(UUID.class)
			.optional();
		return selectedTicketId.flatMap((UUID ticketId) -> {
				int rowsUpdated = jdbcClient.sql("""
						UPDATE tickets
						 SET customer_id=:customerId,
						 status='RESERVED',
						 updated_time=timezone('utc', now())
						 WHERE id=:id AND status='AVAILABLE'
						""")
					.param("customerId", customerId)
					.param("id", ticketId)
					.update();
				return rowsUpdated == 1 ? Optional.of(ticketId) : Optional.empty();
			}
		);
	}

	@Transactional
	public List<UUID> cleanUpExpiredReservations() {
		Instant expiryTime = Instant.from(expiry.addTo(Instant.now()));
		List<UUID> expiredReservationIds = jdbcClient.sql("""
				SELECT id FROM tickets
				WHERE status='RESERVED'
				AND updated_time < :expiryTime
				LIMIT :limit
				FOR UPDATE
				""")
			.param("limit", this.cleanUpBatchSize)
			.param("expiryTime", Timestamp.from(expiryTime))
			.query(UUID.class)
			.list();
		return jdbcClient.sql("""
				UPDATE tickets
				 SET status='AVAILABLE',
				 updated_time=timezone('utc', now())
				 WHERE id IN (:ids) AND status='RESERVED'
				 RETURNING id
				""")
			.param("ids", expiredReservationIds)
			.param("ids", expiredReservationIds)
			.query(UUID.class)
			.list();
	}
}
