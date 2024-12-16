package com.harishkannarao.jdbc.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerBuilderTest {

	@Test
	public void build_without_changes() {
		Customer input = new Customer(1,
			"first-" + UUID.randomUUID(),
			"last-" + UUID.randomUUID());

		Customer result = CustomerBuilder.from(input)
			.build();

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}

	@Test
	public void build_with_changes() {
		Customer input = new Customer(1,
			"first-" + UUID.randomUUID(),
			"last-" + UUID.randomUUID());

		Customer expected = new Customer(1,
			"f-" + UUID.randomUUID(),
			"l-" + UUID.randomUUID());

		Customer result = CustomerBuilder.from(input)
			.firstName(expected.firstName())
			.lastName(expected.lastName())
			.build();

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(expected);
	}
}