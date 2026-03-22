package com.harishkannarao.jdbc.util;

import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.sql.SQLException;

@Component
public class JsonUtil {
	private static final String JSONB_TYPE = "jsonb";

	private final ObjectMapper objectMapper;

	public JsonUtil(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String toJson(Object input) {
		return objectMapper.writeValueAsString(input);
	}


	public <T> T fromJson(String input, Class<T> type) {
		return objectMapper.readValue(input, type);
	}

	public PGobject toPgObject(Object input) {
		PGobject jsondata = new PGobject();
		jsondata.setType(JSONB_TYPE);
		try {
			jsondata.setValue(toJson(input));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return jsondata;
	}

	public <T> T fromPgObject(PGobject input, Class<T> type) {
		return fromJson(input.getValue(), type);
	}
}
