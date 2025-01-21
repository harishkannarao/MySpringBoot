package com.harishkannarao.jdbc.entity.converter;

import com.harishkannarao.jdbc.entity.InventoryDetails;
import com.harishkannarao.jdbc.util.JsonUtil;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
public class InventoryDetailsWriter implements Converter<InventoryDetails, PGobject> {

	private final JsonUtil jsonUtil;

	@Autowired
	public InventoryDetailsWriter(JsonUtil jsonUtil) {
		this.jsonUtil = jsonUtil;
	}

	@Override
	public PGobject convert(@NotNull InventoryDetails source) {
		return jsonUtil.toPgObject(source);
	}
}
