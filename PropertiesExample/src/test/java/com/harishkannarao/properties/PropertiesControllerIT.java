package com.harishkannarao.properties;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PropertiesControllerIT extends BaseIntegrationWithDefaultProperties {

	private final ObjectMapper objectMapper;
	private final RequestSpecification requestSpecification;
	private final ResponseSpecification responseSpecification;

	@Autowired
	public PropertiesControllerIT(
		ObjectMapper objectMapper,
		RequestSpecification requestSpecification,
		ResponseSpecification responseSpecification) {
		this.objectMapper = objectMapper;
		this.requestSpecification = requestSpecification;
		this.responseSpecification = responseSpecification;
	}

	@Test
	public void shouldGetDefaultPropertyValues() {
		ExtractableResponse<Response> response = given(requestSpecification)
			.get()
			.then()
			.spec(responseSpecification)
			.extract();
		assertEquals(200, response.statusCode());
		CustomProperties entity = objectMapper.readValue(response.body().asString(), CustomProperties.class);
		assertEquals("value1", entity.property1());
		assertEquals("value3", entity.property2());
	}

	@Test
	public void shouldGetStringListPropertyValues() {
		ExtractableResponse<Response> response = given(requestSpecification)
			.get("/custom-strings")
			.then()
			.spec(responseSpecification)
			.extract();
		assertEquals(200, response.statusCode());
		CustomStringsProperties entity = objectMapper.readValue(response.body().asString(), CustomStringsProperties.class);
		assertTrue(entity.values().contains("list value 1"));
		assertTrue(entity.values().contains("list-value-2"));
		assertEquals(2, entity.values().size());
	}

	@Test
	public void shouldGetStringListPropertyValues_usingAlternateApproach() {
		ExtractableResponse<Response> response = given(requestSpecification)
			.get("/alternate-custom-strings")
			.then()
			.spec(responseSpecification)
			.extract();
		assertEquals(200, response.statusCode());
		List<String> entity = List.of(objectMapper.readValue(response.body().asString(), String[].class));
		assertTrue(entity.contains("list value 1"));
		assertTrue(entity.contains("list-value-2"));
		assertEquals(2, entity.size());
	}
}
