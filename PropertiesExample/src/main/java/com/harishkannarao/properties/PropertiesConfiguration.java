package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationPropertiesScan
public class PropertiesConfiguration {

	@Bean(name = "customStringsMutable")
	@ConfigurationProperties("custom-strings")
	public List<String> createMutableCustomStrings() {
		return new ArrayList<>();
	}

	@Bean(name = "customStrings")
	public List<String> createImmutableCustomStrings(
		@Qualifier("customStringsMutable") List<String> mutableList) {
		return List.copyOf(mutableList);
	}
}
