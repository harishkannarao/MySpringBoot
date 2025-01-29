package com.harishkannarao.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Set;

@ConfigurationProperties
public record StringListProperties(
	@Name("custom-strings")
	Set<String> listOfStrings
) {
}
