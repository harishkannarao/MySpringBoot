package com.harishkannarao.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PropertiesControllerIT extends BaseIntegrationWithDefaultProperties {

	@Value("${properties.endpoint.url}")
	private String propertiesEndpointUrl;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void shouldGetDefaultPropertyValues() throws JsonProcessingException {
		ResponseEntity<String> response = testRestTemplate.getForEntity(propertiesEndpointUrl, String.class);

		assertEquals(200, response.getStatusCode().value());
		String json = Objects.requireNonNull(response.getBody());
		System.out.println("json = " + json);
		CustomProperties entity = objectMapper.readValue(json, CustomProperties.class);
		assertEquals("value1", entity.property1());
		assertEquals("value2", entity.property2());
	}

	@Test
	public void shouldGetStringListPropertyValues() throws JsonProcessingException {
		ResponseEntity<String> response = testRestTemplate.getForEntity(propertiesEndpointUrl + "/custom-strings", String.class);

		assertEquals(200, response.getStatusCode().value());
		String json = Objects.requireNonNull(response.getBody());
		System.out.println("json = " + json);
		CustomStringsProperties entity = objectMapper.readValue(json, CustomStringsProperties.class);
		assertTrue(entity.values().contains("list value 1"));
		assertTrue(entity.values().contains("list-value-2"));
		assertEquals(2, entity.values().size());
	}
}
