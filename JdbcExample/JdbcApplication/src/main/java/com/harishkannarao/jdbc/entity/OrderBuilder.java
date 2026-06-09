package com.harishkannarao.jdbc.entity;

import java.time.Instant;
import java.util.UUID;

public record OrderBuilder(
	Order build
) {

	public static OrderBuilder from(Order initial) {
		return new OrderBuilder(initial);
	}

	public OrderBuilder customerId(UUID value) {
		return new OrderBuilder(
			new Order(
				build().id(),
				value,
				build.createdTime(),
				build.updatedTime(),
				build.version()
			)
		);
	}

	public OrderBuilder version(Integer value) {
		return new OrderBuilder(
			new Order(
				build().id(),
				build.customerId(),
				build.createdTime(),
				build.updatedTime(),
				value
			)
		);
	}

	public OrderBuilder createdTime(Instant value) {
		return new OrderBuilder(
			new Order(
				build().id(),
				build.customerId(),
				value,
				build.updatedTime(),
				build.version()
			)
		);
	}

	public OrderBuilder updatedTime(Instant value) {
		return new OrderBuilder(
			new Order(
				build().id(),
				build.customerId(),
				build.createdTime(),
				value,
				build.version()
			)
		);
	}
}
