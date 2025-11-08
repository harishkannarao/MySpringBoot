package com.harishkannarao.jdbc.domain;

public record DeeplyImmutableRecordBuilder(
	DeeplyImmutableRecord value
) {
	public static DeeplyImmutableRecordBuilder from(
		DeeplyImmutableRecord input
	) {
		return new DeeplyImmutableRecordBuilder(input);
	}

	public DeeplyImmutableRecordBuilder text(String newValue) {
		return from(
			new DeeplyImmutableRecord(
				value().intField(),
				newValue,
				value().optionalText(),
				value().stringList(),
				value().stringSet(),
				value().stringMap()
			)
		);
	}

	public DeeplyImmutableRecord build() {
		return value;
	}

}
