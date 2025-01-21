package com.harishkannarao.jdbc.entity.converter;

import com.harishkannarao.jdbc.entity.JsonContent;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@WritingConverter
@Component
public class JsonContentWriter implements Converter<JsonContent, PGobject> {
	private static final String JSONB_TYPE = "jsonb";

	@Override
	public PGobject convert(@NotNull JsonContent source) {
		if (!source.data().trim().isEmpty()) {
			PGobject jsondata = new PGobject();
			jsondata.setType(JSONB_TYPE);
			try {
				jsondata.setValue(source.data());
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			return jsondata;
		}
		return null;
	}
}
