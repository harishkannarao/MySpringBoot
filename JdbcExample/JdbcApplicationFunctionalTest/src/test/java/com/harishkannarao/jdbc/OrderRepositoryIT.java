package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.entity.Order;
import com.harishkannarao.jdbc.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
	}
}
