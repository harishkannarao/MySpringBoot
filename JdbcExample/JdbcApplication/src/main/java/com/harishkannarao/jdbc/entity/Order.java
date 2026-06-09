package com.harishkannarao.jdbc.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("orders")
public record Order(
	@Id Long id,
	UUID customerId,
	@InsertOnlyProperty
	Instant createdTime,
	Instant updatedTime,
	@Version Integer version
) {
}
