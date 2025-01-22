package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.entity.Order;
import com.harishkannarao.jdbc.entity.OrderBuilder;
import com.harishkannarao.jdbc.repository.OrderRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class OrderRepositoryIT extends BaseIntegrationJdbc {
	private final OrderRepository orderRepository;

	@Autowired
	public OrderRepositoryIT(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@BeforeEach
	@AfterEach
	public void cleanUp() {
		orderRepository.deleteAll();
	}

	@Test
	public void test_create_read_update_delete_order() {
		Order input = new Order(null, UUID.randomUUID(), null, null, null);
		Order created = orderRepository.save(input);

		assertThat(created.id()).isNotNull();
		assertThat(created.customerId()).isEqualTo(input.customerId());
		assertThat(created.createdTime())
			.isAfterOrEqualTo(Instant.now().minusSeconds(2))
			.isBeforeOrEqualTo(Instant.now().plusSeconds(2));
		assertThat(created.updatedTime())
			.isAfterOrEqualTo(Instant.now().minusSeconds(2))
			.isBeforeOrEqualTo(Instant.now().plusSeconds(2));
		assertThat(created.version()).isEqualTo(0);

		Order toUpdate = OrderBuilder.from(created)
			.customerId(UUID.randomUUID())
			.build();

		orderRepository.save(toUpdate);
		Order updated = orderRepository.findById(toUpdate.id()).orElseThrow();

		assertThat(updated.id()).isEqualTo(created.id());
		assertThat(updated.customerId()).isEqualTo(toUpdate.customerId());
		assertThat(updated.createdTime())
			.isEqualTo(created.createdTime());
		assertThat(updated.updatedTime())
			.isAfterOrEqualTo(created.updatedTime())
			.isBeforeOrEqualTo(Instant.now().plusSeconds(2));
		assertThat(updated.version()).isEqualTo(1);

		Optional<Order> read = orderRepository.findById(created.id());

		assertThat(read)
			.isPresent()
			.hasValueSatisfying(value ->
				assertThat(value)
					.usingRecursiveComparison()
					.ignoringCollectionOrder()
					.isEqualTo(updated));

		orderRepository.deleteById(created.id());

		Optional<Order> afterDelete = orderRepository.findById(created.id());
		assertThat(afterDelete).isEmpty();
	}

	@Test
	public void test_optimistic_error_on_version_mismatch() {
		Order input = new Order(null, UUID.randomUUID(), null, null, null);

		Order created = orderRepository.save(input);
		assertThat(created.version()).isEqualTo(0);

		Order updated = orderRepository.save(created);
		assertThat(updated.version()).isEqualTo(1);

		Order staleUpdate = OrderBuilder.from(created)
			.customerId(UUID.randomUUID())
			.build();

		OptimisticLockingFailureException staleResult = catchThrowableOfType(
			() -> orderRepository.save(staleUpdate),
			OptimisticLockingFailureException.class);
		assertThat(staleResult).isNotNull();

		Order futureUpdate = OrderBuilder.from(updated)
			.customerId(UUID.randomUUID())
			.version(updated.version() + 1)
			.build();

		OptimisticLockingFailureException futureResult = catchThrowableOfType(
			() -> orderRepository.save(futureUpdate),
			OptimisticLockingFailureException.class);
		assertThat(futureResult).isNotNull();
	}

	@Test
	public void test_findAll_by_customer_id() {
		UUID customerId = UUID.randomUUID();

		Long orderId1 = orderRepository.save(
				new Order(null, customerId, null, null, null))
			.id();
		Order order1 = orderRepository.findById(orderId1).orElseThrow();
		Long orderId2 = orderRepository.save(
				new Order(null, customerId, null, null, null))
			.id();
		Order order2 = orderRepository.findById(orderId2).orElseThrow();
		orderRepository.save(
			new Order(null, UUID.randomUUID(), null, null, null));

		List<Order> result = orderRepository.findByCustomerId(customerId);

		assertThat(result)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder().withIgnoreCollectionOrder(true).build())
			.containsExactlyInAnyOrderElementsOf(List.of(order1, order2));
	}

	@Test
	public void test_findAll_by_customer_ids() {
		UUID customerId1 = UUID.randomUUID();
		UUID customerId2 = UUID.randomUUID();

		Long orderId1 = orderRepository.save(
				new Order(null, customerId1, null, null, null))
			.id();
		Order order1 = orderRepository.findById(orderId1).orElseThrow();
		Long orderId2 = orderRepository.save(
				new Order(null, customerId1, null, null, null))
			.id();
		Order order2 = orderRepository.findById(orderId2).orElseThrow();
		Long orderId3 = orderRepository.save(
				new Order(null, customerId2, null, null, null))
			.id();
		Order order3 = orderRepository.findById(orderId3).orElseThrow();
		orderRepository.save(
			new Order(null, UUID.randomUUID(), null, null, null));

		List<Order> result = orderRepository.findAllByCustomerIds(List.of(customerId1, customerId2));

		assertThat(result)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder().withIgnoreCollectionOrder(true).build())
			.containsExactlyInAnyOrderElementsOf(List.of(order1, order2, order3));
	}

	@Test
	public void test_delete_by_customer_ids() {
		UUID customerId1 = UUID.randomUUID();
		UUID customerId2 = UUID.randomUUID();
		UUID customerId3 = UUID.randomUUID();

		Order order1 = orderRepository.save(
			new Order(null, customerId1, null, null, null));
		Order order2 = orderRepository.save(
			new Order(null, customerId1, null, null, null));
		Order order3 = orderRepository.save(
			new Order(null, customerId2, null, null, null));
		Order order4 = orderRepository.save(
			new Order(null, customerId3, null, null, null));

		orderRepository.deleteAllByCustomerIdIn(
			List.of(customerId1, customerId2));

		List<Order> result = orderRepository.findAllById(
			List.of(order1.id(), order2.id(), order3.id()));

		assertThat(result).isEmpty();

		assertThat(orderRepository.findById(order4.id()))
			.isPresent();
	}
}
