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
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
	void test_order_with_documents() throws SQLException {
		Order input = new Order(null, UUID.randomUUID(), null, null, null);
		Order created = orderRepository.save(input);

		OrderDocument document = new OrderDocument(UUID.randomUUID(), created.id(), null);
		OrderDocument createdDocument = orderDocumentRepository.insert(document);

		assertThat(createdDocument)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(document);

		List<OrderDocument> documentsForOrder = orderDocumentRepository.findByOrderIdIn(Set.of(created.id()));

		assertThat(documentsForOrder)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder()
					.withIgnoreCollectionOrder(true)
					.build())
			.containsExactlyInAnyOrder(document);

		String json = """
			{"key": "value"}
			""".trim();
		PGobject jsondata = new PGobject();
		jsondata.setType("jsonb");
		jsondata.setValue(json);
		OrderDocument toUpdate = OrderDocumentBuilder.from(createdDocument)
			.data(jsondata)
			.build();

		orderDocumentRepository.save(toUpdate);

		Optional<OrderDocument> updated = orderDocumentRepository.findById(createdDocument.id());

		assertThat(updated)
			.isPresent()
			.hasValueSatisfying(value ->
				assertThat(value)
					.usingRecursiveComparison()
					.ignoringCollectionOrder()
					.isEqualTo(toUpdate));
	}

	@Test
	void test_find_by_order_id_returns_empty() {
		List<OrderDocument> documentsForOrder = orderDocumentRepository.findByOrderIdIn(Set.of(0L));

		assertThat(documentsForOrder).isEmpty();
	}
}
