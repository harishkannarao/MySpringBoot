package com.harishkannarao.jdbc.entity;

import org.postgresql.util.PGobject;

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
				build().data()
			)
		);
	}

	public OrderDocumentBuilder data(PGobject value) {
		return new OrderDocumentBuilder(
			new OrderDocument(
				build.id(),
				build().orderId(),
				value
			)
		);
	}
}
