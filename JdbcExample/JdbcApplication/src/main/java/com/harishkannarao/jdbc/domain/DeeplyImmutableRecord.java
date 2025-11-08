package com.harishkannarao.jdbc.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.*;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.empty;

public record DeeplyImmutableRecord(
	int intField,
	String text,
	Optional<String> optionalText,
	List<String> stringList,
	Set<String> stringSet,
	Map<String, String> stringMap
) {
	public DeeplyImmutableRecord {
		optionalText = requireNonNullElse(optionalText, empty());
		stringList = List.copyOf(requireNonNullElse(stringList, emptyList()));
		stringSet = Set.copyOf(requireNonNullElse(stringSet, emptySet()));
		stringMap = Map.copyOf(requireNonNullElse(stringMap, emptyMap()));
	}
}
