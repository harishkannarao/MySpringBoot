package com.harishkannarao.jdbc.domain;

import java.util.UUID;

public record TicketBookingResponseDto(
	UUID ticketId,
	UUID customerId) {
}
