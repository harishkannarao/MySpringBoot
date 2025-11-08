package com.harishkannarao.jdbc.domain;

public record DeeplyImmutableRecordBuilder(
	DeeplyImmutableRecord build
) {

	public DeeplyImmutableRecordBuilder text(String newValue) {
		return new DeeplyImmutableRecordBuilder(
			new DeeplyImmutableRecord(
				this.build.intField(),
				newValue,
				this.build.optionalText(),
				this.build.stringList(),
				this.build.stringSet(),
				this.build.stringMap()
			)
		);
	}

}
