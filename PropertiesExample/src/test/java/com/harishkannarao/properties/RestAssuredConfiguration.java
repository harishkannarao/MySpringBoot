package com.harishkannarao.properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

@TestConfiguration
public class RestAssuredConfiguration {

	@Bean
	public RequestSpecification createRequestSpecification(
		@Value("${server.port}") int port
	) {
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.setBaseUri("http://localhost:" + port);
		requestSpecBuilder.setContentType(MediaType.APPLICATION_JSON_VALUE);
		requestSpecBuilder.setAccept(MediaType.APPLICATION_JSON_VALUE);
		requestSpecBuilder.log(LogDetail.ALL);
		return requestSpecBuilder.build();
	}

	@Bean
	public ResponseSpecification createResponseSpecification() {
		ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
		responseSpecBuilder.log(LogDetail.ALL);
		return responseSpecBuilder.build();
	}
}
