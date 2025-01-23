package com.harishkannarao.jdbc.entity;

import java.util.List;
import java.util.Set;

public record InventoryDetails(
	String productCode,
	int quantity,
	List<Sku> skuList,
	Set<String> labels) {
}
