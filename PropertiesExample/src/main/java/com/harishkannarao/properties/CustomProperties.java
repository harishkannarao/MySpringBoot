package com.harishkannarao.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties("custom")
public record CustomProperties(
	@JsonProperty("Property1")
	@Name("prop1")
	String property1,
	@JsonProperty("Property2")
	@Name("prop2")
	String property2
) {
}
