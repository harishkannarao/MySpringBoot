package com.harishkannarao.jdbc.entity.converter;

import com.harishkannarao.jdbc.entity.InventoryDetails;
import com.harishkannarao.jdbc.util.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class InventoryDetailsReader implements Converter<PGobject, InventoryDetails> {

	private final JsonUtil jsonUtil;

	public InventoryDetailsReader(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	@Override
	public InventoryDetails convert(@NotNull PGobject source) {
		return jsonUtil.fromPgObject(source, InventoryDetails.class);
	}
}
