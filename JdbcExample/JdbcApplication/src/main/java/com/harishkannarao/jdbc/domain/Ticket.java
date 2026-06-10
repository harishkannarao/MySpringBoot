package com.harishkannarao.jdbc.domain;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public record Ticket(
	UUID id,
	String status,
	@Nullable UUID customerId,
	@Nullable Instant updatedTime
) {
}
