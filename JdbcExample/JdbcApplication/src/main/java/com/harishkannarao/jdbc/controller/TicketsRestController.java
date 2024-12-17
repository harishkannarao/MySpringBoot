package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.dao.TicketDao;
import com.harishkannarao.jdbc.domain.Ticket;
import com.harishkannarao.jdbc.domain.TicketReservationResponseDto;
import com.harishkannarao.jdbc.domain.TicketsAvailabilityResponseDto;
import com.harishkannarao.jdbc.domain.TicketsCleanupResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping(
	value = "/tickets",
	consumes = {MediaType.APPLICATION_JSON_VALUE},
	produces = {MediaType.APPLICATION_JSON_VALUE})
public class TicketsRestController {

	private final TicketDao ticketDao;

	@Autowired
	public TicketsRestController(TicketDao ticketDao) {
		this.ticketDao = ticketDao;
	}

	@RequestMapping(
		value = "/available",
		method = RequestMethod.GET)
	public ResponseEntity<TicketsAvailabilityResponseDto> getAvailableCount() {
		return ResponseEntity.ok(new TicketsAvailabilityResponseDto(ticketDao.getAvailableTickets()));
	}

	@RequestMapping(
		value = "/create/{count}",
		method = RequestMethod.PUT)
	public ResponseEntity<Set<UUID>> createTickets(
		@PathVariable("count") int count
	) {
		Set<UUID> createdTicketIds = IntStream.rangeClosed(1, count)
			.boxed()
			.map(integer -> new Ticket(UUID.randomUUID(), "AVAILABLE", null, null))
			.peek(ticketDao::create)
			.map(Ticket::id)
			.collect(Collectors.toUnmodifiableSet());
		return ResponseEntity.ok(createdTicketIds);
	}

	@RequestMapping(
		value = "/reserve/{customerId}",
		method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<TicketReservationResponseDto> reserveTicketForCustomer(
		@PathVariable("customerId") UUID customerId
	) {
		return ResponseEntity.ok(
			new TicketReservationResponseDto(
				ticketDao.reserveTicket(customerId).orElse(null)));
	}

	@RequestMapping(
		value = "/cleanup-reservations",
		method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity<TicketsCleanupResponseDto> cleanupReservations() {
		return ResponseEntity.ok(
			new TicketsCleanupResponseDto(
				ticketDao.cleanUpExpiredReservations()));
	}

}
