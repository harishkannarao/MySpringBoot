package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.entity.Order;
import com.harishkannarao.jdbc.entity.OrderDocument;
import com.harishkannarao.jdbc.entity.OrderDocumentBuilder;
import com.harishkannarao.jdbc.repository.OrderDocumentRepository;
import com.harishkannarao.jdbc.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderDocumentRepositoryIT extends BaseIntegrationJdbc {
	private final OrderRepository orderRepository;
	private final OrderDocumentRepository orderDocumentRepository;

	@Autowired
	public OrderDocumentRepositoryIT(
		OrderRepository orderRepository,
		OrderDocumentRepository orderDocumentRepository) {
		this.orderRepository = orderRepository;
		this.orderDocumentRepository = orderDocumentRepository;
	}

	@BeforeEach
	@AfterEach
	public void cleanUp() {
		orderDocumentRepository.deleteAll();
		orderRepository.deleteAll();
	}

	@Test
	void test_order_with_documents() {
		Order input = new Order(null, UUID.randomUUID(), null, null, null);
		Order created = orderRepository.save(input);

		OrderDocument document = new OrderDocument(UUID.randomUUID(), created.id(), null, null);
		OrderDocument savedDocument = orderDocumentRepository.save(document);

		OrderDocument expected = OrderDocumentBuilder.from(document)
			.version(0)
			.build();

		assertThat(savedDocument)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(expected);

		List<OrderDocument> documentsForOrder = orderDocumentRepository.findByOrderIdIn(Set.of(created.id()));

		assertThat(documentsForOrder)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder()
					.withIgnoreCollectionOrder(true)
					.build())
			.containsExactlyInAnyOrder(expected);
	}
}
