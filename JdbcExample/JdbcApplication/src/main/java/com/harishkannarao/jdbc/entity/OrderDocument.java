package com.harishkannarao.jdbc.entity;

import com.harishkannarao.jdbc.entity.type.JsonContent;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_documents")
public record OrderDocument(
	@Id UUID id,
	Long orderId,
	JsonContent data
	) {
}
