package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.TicketBookingResponseDto;
import com.harishkannarao.jdbc.domain.TicketReservationResponseDto;
import com.harishkannarao.jdbc.domain.TicketsAvailabilityResponseDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketReservationIT extends BaseIntegrationJdbc {

	private final TicketTestSupportDao ticketTestSupportDao;
	private final String createTicketsEndpoint;
	private final String getTicketsAvailabilityEndpoint;
	private final String reserveTicketEndpoint;
	private final String bookTicketEndpoint;

	@Autowired
	public TicketReservationIT(
		TicketTestSupportDao ticketTestSupportDao,
		@Value("${createTicketEndpointUrl}") String createTicketsEndpoint,
		@Value("${getTicketsAvailabilityEndpointUrl}") String getTicketsAvailabilityEndpoint,
		@Value("${reserveTicketEndpointUrl}") String reserveTicketEndpoint,
		@Value("${bookTicketEndpointUrl}") String bookTicketEndpoint) {
		this.ticketTestSupportDao = ticketTestSupportDao;
		this.createTicketsEndpoint = createTicketsEndpoint;
		this.getTicketsAvailabilityEndpoint = getTicketsAvailabilityEndpoint;
		this.reserveTicketEndpoint = reserveTicketEndpoint;
		this.bookTicketEndpoint = bookTicketEndpoint;
	}

	@BeforeEach
	public void setUp() {
		ticketTestSupportDao.deleteAll();
	}

	@Test
	public void create_tickets_and_reserve_tickets_and_book_tickets_without_loss() {
		ResponseEntity<String[]> createResp = restClient
			.put()
			.uri(createTicketsEndpoint, Map.of("count", 100L))
			.retrieve()
			.toEntity(String[].class);

		assertThat(createResp.getStatusCode().value()).isEqualTo(200);
		List<String> createdTicketIds = Arrays.stream(requireNonNull(createResp.getBody())).toList();
		assertThat(createdTicketIds).hasSize(100);

		ResponseEntity<TicketsAvailabilityResponseDto> availableTicketsBeforeReservation = restClient
			.get()
			.uri(getTicketsAvailabilityEndpoint)
			.retrieve()
			.toEntity(TicketsAvailabilityResponseDto.class);

		assertThat(availableTicketsBeforeReservation.getStatusCode().value()).isEqualTo(200);
		assertThat(requireNonNull(availableTicketsBeforeReservation.getBody()).available()).isEqualTo(100);

		List<CompletableFuture<ResponseEntity<TicketReservationResponseDto>>> reservationRequests =
			IntStream.rangeClosed(1, 100)
				.boxed()
				.parallel()
				.map(integer ->
					CompletableFuture.supplyAsync(() ->
						restClient
							.post()
							.uri(reserveTicketEndpoint, UUID.randomUUID().toString())
							.retrieve()
							.toEntity(TicketReservationResponseDto.class)))
				.toList();

		List<ResponseEntity<TicketReservationResponseDto>> reservationResponses = reservationRequests.stream()
			.map(CompletableFuture::join)
			.toList();

		assertThat(reservationResponses)
			.allSatisfy(resp -> {
				assertThat(resp.getStatusCode().value()).isEqualTo(200);
				assertThat(requireNonNull(resp.getBody()).ticketId()).isNotNull();
				assertThat(requireNonNull(resp.getBody()).customerId()).isNotNull();
			});

		List<String> reservedTickedIds = reservationResponses.stream()
			.map(resp -> requireNonNull(resp.getBody()).ticketId())
			.map(UUID::toString)
			.toList();

		assertThat(reservedTickedIds)
			.hasSize(100)
			.containsExactlyInAnyOrderElementsOf(createdTicketIds);

		ResponseEntity<TicketsAvailabilityResponseDto> availableTicketsAfterReservation = restClient
			.get()
			.uri(getTicketsAvailabilityEndpoint)
			.retrieve()
			.toEntity(TicketsAvailabilityResponseDto.class);

		assertThat(availableTicketsAfterReservation.getStatusCode().value()).isEqualTo(200);
		assertThat(requireNonNull(availableTicketsAfterReservation.getBody()).available()).isEqualTo(0);

		var bookingRequests = reservationResponses.stream()
			.parallel()
			.map(entity -> requireNonNull(entity.getBody()))
			.map(reservation -> CompletableFuture.supplyAsync(() ->
				restClient
					.post()
					.uri(bookTicketEndpoint, reservation.ticketId(), reservation.customerId())
					.retrieve()
					.toEntity(TicketBookingResponseDto.class))
			)
			.toList();

		List<ResponseEntity<TicketBookingResponseDto>> bookingResponses = bookingRequests.stream()
			.parallel()
			.map(CompletableFuture::join)
			.toList();

		assertThat(bookingResponses)
			.allSatisfy(it ->
				assertThat(it.getStatusCode().value()).isEqualTo(200));

		Map<Boolean, List<TicketBookingResponseDto>> bookingsMapping = bookingResponses.stream()
			.map(it -> requireNonNull(it.getBody()))
			.collect(Collectors.partitioningBy(o -> o.customerId() != null));

		List<TicketBookingResponseDto> successfulBookings = bookingsMapping.get(true);
		List<TicketBookingResponseDto> unsuccessfulBookings = bookingsMapping.get(false);

		assertThat(successfulBookings)
			.hasSameSizeAs(reservedTickedIds);
		assertThat(unsuccessfulBookings).isEmpty();

		Set<UUID> uniqueCustomerIds = successfulBookings.stream()
			.map(TicketBookingResponseDto::customerId)
			.collect(Collectors.toUnmodifiableSet());

		assertThat(uniqueCustomerIds).hasSize(100);

		for(var reservationResponse : reservationResponses) {
			var reservation = requireNonNull(reservationResponse.getBody());
			assertThat(successfulBookings)
				.anySatisfy(booking -> {
					assertThat(booking.ticketId()).isEqualTo(reservation.ticketId());
					assertThat(booking.customerId()).isEqualTo(reservation.customerId());
				});
		}
	}
}
