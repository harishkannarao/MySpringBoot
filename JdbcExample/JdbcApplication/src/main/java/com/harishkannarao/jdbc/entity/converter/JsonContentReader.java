package com.harishkannarao.jdbc.entity.converter;

import com.harishkannarao.jdbc.entity.JsonContent;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@ReadingConverter
@Component
public class JsonContentReader implements Converter<PGobject, JsonContent> {
	private static final String JSONB_TYPE = "jsonb";

	@Override
	public JsonContent convert(@NotNull PGobject source) {
		if (Objects.nonNull(source.getValue())
			&& JSONB_TYPE.equals(source.getType())
			&& !source.getValue().trim().isEmpty()) {
			return new JsonContent(source.getValue());
		}
		return null;
	}
}
