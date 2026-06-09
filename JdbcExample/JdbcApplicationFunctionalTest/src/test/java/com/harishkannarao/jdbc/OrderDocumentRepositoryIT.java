package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.dao.repository.OrderDocumentRepository;
import com.harishkannarao.jdbc.dao.repository.OrderRepository;
import com.harishkannarao.jdbc.entity.InventoryDetails;
import com.harishkannarao.jdbc.entity.JsonContent;
import com.harishkannarao.jdbc.entity.Order;
import com.harishkannarao.jdbc.entity.OrderDocument;
import com.harishkannarao.jdbc.entity.OrderDocumentBuilder;
import com.harishkannarao.jdbc.entity.Sku;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

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
		Order input = new Order(UUID.randomUUID());
		Order created = orderRepository.save(input);

		OrderDocument document = new OrderDocument(UUID.randomUUID(), created.id(), null, null);
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
		OrderDocument toUpdate = OrderDocumentBuilder.from(createdDocument)
			.data(new JsonContent(json))
			.inventory(new InventoryDetails(
				"abc",
				4,
				List.of(new Sku("3434"), new Sku("235453")),
				Set.of("234445", "2234243"))
			)
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
	void test_query_by_json_key() throws SQLException {
		Order order1 = new Order(UUID.randomUUID());
		Order created1 = orderRepository.save(order1);

		Order order2 = new Order(null, UUID.randomUUID(), null, null, null);
		Order created2 = orderRepository.save(order2);

		String json1 = """
			{"name": "test1", "department": "finance"}
			""".trim();
		InventoryDetails inventoryDetails1 = new InventoryDetails("abc", 2, null, null);
		OrderDocument document1 = new OrderDocument(
			UUID.randomUUID(),
			created1.id(),
			new JsonContent(json1),
			inventoryDetails1);
		orderDocumentRepository.insert(document1);

		String json2 = """
			{"name": "test2", "department": "hr"}
			""".trim();
		InventoryDetails inventoryDetails2 = new InventoryDetails("xyz", 3, null, null);
		OrderDocument document2 = new OrderDocument(
			UUID.randomUUID(),
			created2.id(),
			new JsonContent(json2),
			inventoryDetails2);
		orderDocumentRepository.insert(document2);

		List<OrderDocument> byName = orderDocumentRepository.findByJsonAttribute("name", "test1");
		assertThat(byName)
			.hasSize(1)
			.anySatisfy(value -> assertThat(value.id()).isEqualTo(document1.id()));

		List<OrderDocument> byDepartment = orderDocumentRepository.findByJsonAttribute("department", "hr");
		assertThat(byDepartment)
			.hasSize(1)
			.anySatisfy(value -> assertThat(value.id()).isEqualTo(document2.id()));

		List<OrderDocument> byProductCode = orderDocumentRepository.findByProductCode("xyz");
		assertThat(byProductCode)
			.hasSize(1)
			.anySatisfy(value -> assertThat(value.id()).isEqualTo(document2.id()));

		List<OrderDocument> byNonExistent = orderDocumentRepository.findByProductCode("non-existent");
		assertThat(byNonExistent).isEmpty();
	}

	@Test
	void test_find_by_order_id_returns_empty() {
		List<OrderDocument> documentsForOrder = orderDocumentRepository.findByOrderIdIn(Set.of(0L));

		assertThat(documentsForOrder).isEmpty();
	}

	@Test
	void test_insert_returns_duplicate_key_exception() {
		Order input = new Order(UUID.randomUUID());
		Order created = orderRepository.save(input);

		UUID documentId = UUID.randomUUID();
		OrderDocument document1 = new OrderDocument(documentId, created.id(), null, null);
		OrderDocument document2 = new OrderDocument(documentId, created.id(), null, null);
		orderDocumentRepository.insert(document1);

		DuplicateKeyException result = catchThrowableOfType(
			() -> orderDocumentRepository.insert(document2),
			DuplicateKeyException.class);

		assertThat(result).isNotNull();
	}

	@Test
	void test_save_inserts_or_updates_existing_with_same_document_id() {
		Order input = new Order(UUID.randomUUID());
		Order created = orderRepository.save(input);

		UUID documentId = UUID.randomUUID();
		OrderDocument document1 = new OrderDocument(documentId, created.id(), null, null);
		OrderDocument document2 = new OrderDocument(
			documentId,
			created.id(),
			null,
			new InventoryDetails(
				"abc",
				2,
				List.of(new Sku("3434"), new Sku("235453")),
				Set.of("234445", "2234243")
			)
		);
		OrderDocument inserted = orderDocumentRepository.save(document1);
		assertThat(inserted)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(document1);

		List<OrderDocument> listByIdAfterInsert = orderDocumentRepository.findAllById(List.of(documentId));
		assertThat(listByIdAfterInsert)
			.hasSize(1)
			.anySatisfy(orderDocument -> assertThat(orderDocument)
				.usingRecursiveComparison()
				.ignoringCollectionOrder()
				.isEqualTo(document1));

		OrderDocument finalUpdate = orderDocumentRepository.save(document2);

		assertThat(finalUpdate)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(document2);

		List<OrderDocument> listByIdAfterUpdate = orderDocumentRepository.findAllById(List.of(documentId));
		assertThat(listByIdAfterUpdate)
			.hasSize(1)
			.anySatisfy(orderDocument -> assertThat(orderDocument)
				.usingRecursiveComparison()
				.ignoringCollectionOrder()
				.isEqualTo(document2));
	}

	@Test
	void test_upsert_inserts_or_updates_existing_without_document_id() {
		Order input = new Order(UUID.randomUUID());
		Order created = orderRepository.save(input);

		OrderDocument document1 = new OrderDocument(null, created.id(), null, null);
		OrderDocument document2 = new OrderDocument(
			null,
			created.id(),
			null,
			new InventoryDetails(
				"abc",
				2,
				List.of(new Sku("3434"), new Sku("235453")),
				Set.of("234445", "2234243")
			)
		);
		OrderDocument inserted = orderDocumentRepository.upsert(document1.orderId(), document1.data(), document1.inventory());
		assertThat(inserted)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.ignoringFields("id")
			.isEqualTo(document1);
		assertThat(inserted.id()).isNotNull();

		List<OrderDocument> listByIdAfterInsert = orderDocumentRepository.findAllById(List.of(inserted.id()));
		assertThat(listByIdAfterInsert)
			.hasSize(1)
			.anySatisfy(orderDocument -> assertThat(orderDocument)
				.usingRecursiveComparison()
				.ignoringCollectionOrder()
				.ignoringFields("id")
				.isEqualTo(document1));

		OrderDocument finalUpdate = orderDocumentRepository.upsert(document2.orderId(), document2.data(), document2.inventory());

		assertThat(finalUpdate)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.ignoringFields("id")
			.isEqualTo(document2);
		assertThat(finalUpdate.id()).isEqualTo(inserted.id());

		List<OrderDocument> listByIdAfterUpdate = orderDocumentRepository.findAllById(List.of(inserted.id()));
		assertThat(listByIdAfterUpdate)
			.hasSize(1)
			.anySatisfy(orderDocument -> assertThat(orderDocument)
				.usingRecursiveComparison()
				.ignoringCollectionOrder()
				.ignoringFields("id")
				.isEqualTo(document2));
	}

	@Test
	void test_insert_multiple_entities() {
		Order order1 = new Order(null, UUID.randomUUID(), null, null, null);
		Order order2 = new Order(null, UUID.randomUUID(), null, null, null);
		Order created1 = orderRepository.save(order1);
		Order created2 = orderRepository.save(order2);

		OrderDocument document1 = new OrderDocument(UUID.randomUUID(), created1.id(), null, null);
		OrderDocument document2 = new OrderDocument(UUID.randomUUID(), created2.id(), null, null);

		List<OrderDocument> insertResult = orderDocumentRepository.insertAll(List.of(document1, document2));

		assertThat(insertResult)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder().withIgnoreCollectionOrder(true).build())
			.containsExactlyInAnyOrder(document1, document2);

		List<OrderDocument> result = orderDocumentRepository.findAllById(List.of(document1.id(), document2.id()));

		assertThat(result)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder().withIgnoreCollectionOrder(true).build())
			.containsExactlyInAnyOrder(document1, document2);
	}
}
