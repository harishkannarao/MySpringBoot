package com.harishkannarao.jdbc.entity;

import org.postgresql.util.PGobject;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_documents")
public record OrderDocument(
	@Id UUID id,
	Long orderId,
	PGobject data
	) {
}
