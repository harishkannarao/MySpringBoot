package com.harishkannarao.jdbc.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DeeplyImmutableRecordTest {

	@Test
	public void test_builder() {
		final DeeplyImmutableRecord input = new DeeplyImmutableRecord(
			2,
			"test-name",
			Optional.empty(),
			List.of("abc"),
			Set.of("def"),
			Map.of("ghi", "jkl")
		);

		final DeeplyImmutableRecord result = DeeplyImmutableRecordBuilder.from(input)
			.text("new-test-name")
			.build();

		assertThat(result.text()).isEqualTo("new-test-name");

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringFields("text")
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}
}
