package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.TicketsAvailabilityResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketReservationIT extends BaseIntegrationJdbc {

	private final TicketTestSupportDao ticketTestSupportDao;
	private final String createTicketsEndpoint;
	private final String getTicketsAvailabilityEndpoint;

	@Autowired
	public TicketReservationIT(
		TicketTestSupportDao ticketTestSupportDao,
		@Value("${createTicketEndpointUrl}") String createTicketsEndpoint,
		@Value("${getTicketsAvailabilityEndpointUrl}") String getTicketsAvailabilityEndpoint) {
		this.ticketTestSupportDao = ticketTestSupportDao;
		this.createTicketsEndpoint = createTicketsEndpoint;
		this.getTicketsAvailabilityEndpoint = getTicketsAvailabilityEndpoint;
	}

	@BeforeEach
	public void setUp() {
		ticketTestSupportDao.deleteAll();
	}

	@Test
	public void create_tickets_and_check_availability() {
		ResponseEntity<String[]> createResp = restClient
			.put()
			.uri(createTicketsEndpoint, Map.of("count", 100L))
			.retrieve()
			.toEntity(String[].class);

		assertThat(createResp.getStatusCode().value()).isEqualTo(200);
		assertThat(createResp.getBody()).hasSize(100);

		ResponseEntity<TicketsAvailabilityResponseDto> ticketAvailabilityResp = restClient
			.get()
			.uri(getTicketsAvailabilityEndpoint)
			.retrieve()
			.toEntity(TicketsAvailabilityResponseDto.class);

		assertThat(ticketAvailabilityResp.getStatusCode().value()).isEqualTo(200);
		assertThat(requireNonNull(ticketAvailabilityResp.getBody()).available()).isEqualTo(100);
	}
}
