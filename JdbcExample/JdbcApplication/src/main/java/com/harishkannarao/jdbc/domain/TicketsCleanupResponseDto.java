package com.harishkannarao.jdbc.domain;

import java.util.List;
import java.util.UUID;

public record TicketsCleanupResponseDto(
	List<UUID> ticketIds
) {
}
