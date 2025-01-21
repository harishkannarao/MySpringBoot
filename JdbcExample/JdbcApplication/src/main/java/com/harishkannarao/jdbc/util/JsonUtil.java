package com.harishkannarao.jdbc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class JsonUtil {
	private static final String JSONB_TYPE = "jsonb";

	private final ObjectMapper objectMapper;

	public JsonUtil(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String toJson(Object input) {
		try {
			return objectMapper.writeValueAsString(input);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}


	public <T> T fromJson(String input, Class<T> type) {
		try {
			return objectMapper.readValue(input, type);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
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
