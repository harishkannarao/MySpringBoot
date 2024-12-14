package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.dao.TicketDao;
import com.harishkannarao.jdbc.domain.Ticket;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketDaoIT extends BaseIntegrationJdbc {

	private final TicketDao ticketDao;

	@Autowired
	public TicketDaoIT(TicketDao ticketDao) {
		this.ticketDao = ticketDao;
	}

	@Test
	public void create_and_getAll_tickets() {
		Ticket input = createTicket();
		ticketDao.create(input);

		List<Ticket> result = ticketDao.getAll();

		assertThat(result)
			.anySatisfy(ticket -> {
					assertThat(ticket)
						.usingRecursiveComparison()
						.ignoringCollectionOrder()
						.ignoringFields("updatedTime")
						.isEqualTo(input);
					assertThat(ticket.updatedTime())
						.isAfterOrEqualTo(Instant.now().minusSeconds(2))
						.isBeforeOrEqualTo(Instant.now().plusSeconds(2));
				}
			);
	}

	@Test
	public void deleteAll_tickets() {
		Ticket input = createTicket();
		ticketDao.create(input);

		assertThat(ticketDao.getAll())
			.isNotEmpty();

		ticketDao.deleteAll();

		assertThat(ticketDao.getAll())
			.isEmpty();
	}

	private static Ticket createTicket() {
		return new Ticket(
			UUID.randomUUID(),
			"AVAILABLE",
			null,
			null);
	}
}
