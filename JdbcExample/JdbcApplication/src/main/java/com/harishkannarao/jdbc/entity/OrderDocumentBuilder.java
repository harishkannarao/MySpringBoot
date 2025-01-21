package com.harishkannarao.jdbc.entity;

public record OrderDocumentBuilder(
	OrderDocument build
) {

	public static OrderDocumentBuilder from(OrderDocument initial) {
		return new OrderDocumentBuilder(initial);
	}

	public OrderDocumentBuilder orderId(Long value) {
		return new OrderDocumentBuilder(
			new OrderDocument(
				build.id(),
				value,
				build.data(),
				build.inventory()
			)
		);
	}

	public OrderDocumentBuilder data(JsonContent value) {
		return new OrderDocumentBuilder(
			new OrderDocument(
				build.id(),
				build.orderId(),
				value,
				build.inventory()
			)
		);
	}

	public OrderDocumentBuilder inventory(InventoryDetails value) {
		return new OrderDocumentBuilder(
			new OrderDocument(
				build.id(),
				build.orderId(),
				build.data(),
				value
			)
		);
	}
}
