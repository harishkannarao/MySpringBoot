package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.dao.TicketDao;
import com.harishkannarao.jdbc.domain.Ticket;
import com.harishkannarao.jdbc.domain.TicketBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketDaoIT extends BaseIntegrationJdbc {

	private final TicketDao ticketDao;
	private final TicketTestSupportDao ticketTestSupportDao;

	@Autowired
	public TicketDaoIT(TicketDao ticketDao,
										 TicketTestSupportDao ticketTestSupportDao) {
		this.ticketDao = ticketDao;
		this.ticketTestSupportDao = ticketTestSupportDao;
	}

	@BeforeEach
	public void setUp() {
		ticketTestSupportDao.deleteAll();
	}

	@Test
	public void create_and_getAll_tickets() {
		Ticket input = createTicket();
		ticketDao.create(input);

		List<Ticket> result = ticketTestSupportDao.getAll();

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
	public void getAvailable_Tickets() {
		Ticket ticket1 = TicketBuilder.from(createTicket())
			.withStatus("AVAILABLE")
			.build();
		Ticket ticket2 = TicketBuilder.from(createTicket())
			.withStatus("RESERVED")
			.build();
		ticketDao.create(ticket1);
		ticketDao.create(ticket2);

		assertThat(ticketDao.getAvailableTickets())
			.isEqualTo(1);

		ticketTestSupportDao.deleteAll();

		assertThat(ticketDao.getAvailableTickets())
			.isEqualTo(0);
	}

	@Test
	public void reserveTicket_forCustomer_successfully() {
		Ticket ticket = TicketBuilder.from(createTicket())
			.withStatus("AVAILABLE")
			.build();
		ticketDao.create(ticket);

		assertThat(ticketDao.getAvailableTickets())
			.isEqualTo(1);

		UUID customerId = UUID.randomUUID();
		Optional<UUID> ticketId = ticketDao.reserveTicket(customerId);

		assertThat(ticketId)
			.isNotEmpty()
			.hasValue(ticket.id());

		assertThat(ticketDao.getAvailableTickets())
			.isEqualTo(0);

		assertThat(ticketTestSupportDao.getAll())
			.anySatisfy(ticket1 -> {
				assertThat(ticket1.id()).isEqualTo(ticket.id());
				assertThat(ticket1.customerId()).isEqualTo(customerId);
			});
	}

	@Test
	public void reserveTicket_returnsEmpty_whenNoAvailableTickets() {
		Ticket ticket = TicketBuilder.from(createTicket())
			.withStatus("BOOKED")
			.build();
		ticketDao.create(ticket);

		assertThat(ticketDao.getAvailableTickets())
			.isEqualTo(0);

		UUID customerId = UUID.randomUUID();
		Optional<UUID> ticketId = ticketDao.reserveTicket(customerId);

		assertThat(ticketId)
			.isEmpty();
	}

	@Test
	public void deleteAll_tickets() {
		Ticket input = createTicket();
		ticketDao.create(input);

		assertThat(ticketTestSupportDao.getAll())
			.isNotEmpty();

		ticketTestSupportDao.deleteAll();

		assertThat(ticketTestSupportDao.getAll())
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
