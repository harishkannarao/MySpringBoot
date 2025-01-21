package com.harishkannarao.jdbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_documents")
public record OrderDocument(
	@Id UUID id,
	Long orderId,
	JsonContent data,
	InventoryDetails inventory) {
}
