package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.dao.repository.OrderRepository;
import com.harishkannarao.jdbc.entity.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"default", "h2-db"})
public class OrderRepositoryTest {
	private final OrderRepository orderRepository;

	@Autowired
	public OrderRepositoryTest(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@BeforeEach
	public void setUp() {
		this.orderRepository.deleteAll();
	}

	@AfterEach
	public void tearDown() {
		this.orderRepository.deleteAll();
	}

	@Test
	public void simple() {
		Order order = new Order(2L, UUID.randomUUID(), null, null, null);
		orderRepository.save(order);
		List<Order> dbEntities = orderRepository.findAll();
		assertThat(dbEntities)
			.hasSize(1)
			.anySatisfy(actual -> {
				assertThat(actual)
					.usingRecursiveComparison()
					.ignoringCollectionOrder()
					.ignoringFields("createdTime", "updatedTime", "version")
					.isEqualTo(order);
				assertThat(actual.createdTime())
					.isAfterOrEqualTo(Instant.now().minusSeconds(2))
					.isBeforeOrEqualTo(Instant.now().plusSeconds(2));
				assertThat(actual.updatedTime())
					.isAfterOrEqualTo(Instant.now().minusSeconds(2))
					.isBeforeOrEqualTo(Instant.now().plusSeconds(2));
				assertThat(actual.version()).isEqualTo(0);
			});
	}
}
