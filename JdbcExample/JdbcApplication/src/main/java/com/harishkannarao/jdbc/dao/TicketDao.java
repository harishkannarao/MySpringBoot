package com.harishkannarao.jdbc.dao;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
				ORDER BY id ASC
				LIMIT 1
				FOR UPDATE SKIP LOCKED
				""")
			.query(UUID.class)
			.optional();
		try {
			// artificial random delay up to 1 second
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
	public boolean bookReservation(UUID customerId, UUID ticketId) {
		Optional<UUID> selectedTicketId = jdbcClient.sql("""
				SELECT id FROM tickets
				 WHERE id=:ticketId
				 AND customer_id=:customerId
				 AND status='RESERVED'
				 FOR UPDATE
				""")
			.param("ticketId", ticketId)
			.param("customerId", customerId)
			.query(UUID.class)
			.optional();
		try {
			// artificial random delay up to 1 second
			Thread.sleep(new Random().nextInt(1000));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		Optional<Boolean> updateResult = selectedTicketId.map((UUID id) -> {
				int rowsUpdated = jdbcClient.sql("""
						UPDATE tickets
						 SET status='BOOKED',
						 updated_time=timezone('utc', now())
						 WHERE id=:ticketId
						 AND customer_id=:customerId
						 AND status='RESERVED'
						""")
					.param("customerId", customerId)
					.param("ticketId", id)
					.update();
				return rowsUpdated == 1;
			}
		);
		return updateResult.orElse(false);
	}

	@Transactional
	public List<UUID> cleanUpExpiredReservations() {
		Instant expiryTime = Instant.from(expiry.addTo(Instant.now()));
		List<UUID> expiredReservationIds = jdbcClient.sql("""
				SELECT id FROM tickets
				WHERE status='RESERVED'
				AND updated_time < :expiryTime
				ORDER BY id ASC
				LIMIT :limit
				FOR UPDATE SKIP LOCKED
				""")
			.param("limit", this.cleanUpBatchSize)
			.param("expiryTime", Timestamp.from(expiryTime))
			.query(UUID.class)
			.list();
		if (expiredReservationIds.isEmpty()) {
			return Collections.emptyList();
		}
		return jdbcClient.sql("""
				UPDATE tickets
				 SET status='AVAILABLE',
				 updated_time=timezone('utc', now())
				 WHERE id IN (:ids) AND status='RESERVED'
				 RETURNING id
				""")
			.param("ids", expiredReservationIds)
			.query(UUID.class)
			.list();
	}
}
