package com.harishkannarao.jdbc.domain;

public record TicketBuilder(
	Ticket build
) {
	public static TicketBuilder from(Ticket initial) {
		return new TicketBuilder(initial);
	}

	public TicketBuilder withStatus(String status) {
		return new TicketBuilder(
			new Ticket(
				build.id(),
				status,
				build.customerId(),
				build.updatedTime()
			)
		);
	}
}
