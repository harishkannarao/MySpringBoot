package com.harishkannarao.jdbc.domain;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
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

		final DeeplyImmutableRecord result = new DeeplyImmutableRecordBuilder(input)
			.text("new-test-name")
			.build();

		assertThat(result.text()).isEqualTo("new-test-name");

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringFields("text")
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}

	@Test
	public void test_list_assertion() {
		final DeeplyImmutableRecord record1 = new DeeplyImmutableRecord(
			2,
			"test-name-2",
			Optional.of("optional-text"),
			List.of("abc", "def"),
			Set.of("def", "ghi"),
			Map.of("ghi", "jkl")
		);
		final DeeplyImmutableRecord record1Copy = new DeeplyImmutableRecord(
			2,
			"test-name-2",
			Optional.of("optional-text"),
			List.of("def", "abc"),
			Set.of("ghi", "def"),
			Map.of("ghi", "jkl")
		);
		final DeeplyImmutableRecord record2 = new DeeplyImmutableRecord(
			3,
			"test-name-3",
			Optional.empty(),
			null,
			null,
			null
		);
		final DeeplyImmutableRecord record2Copy = new DeeplyImmutableRecordBuilder(record2).build();

		List<DeeplyImmutableRecord> actual = List.of(record1, record2);
		List<DeeplyImmutableRecord> expected = List.of(record1Copy, record2Copy);

		assertThat(actual)
			.usingRecursiveFieldByFieldElementComparator(
				RecursiveComparisonConfiguration.builder().withIgnoreCollectionOrder(true).build())
			.containsExactlyElementsOf(expected);
	}
}
