package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.entity.Order;
import com.harishkannarao.jdbc.entity.OrderBuilder;
import com.harishkannarao.jdbc.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.Instant;
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

		Order updated = orderRepository.save(toUpdate);

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
}
