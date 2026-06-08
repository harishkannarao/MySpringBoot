package com.harishkannarao.rest.functional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harishkannarao.rest.controller.CustomApplicationErrorController;
import com.harishkannarao.rest.rule.LogbackTestFileAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorPageIT extends BaseIntegration {

	private static final String ERROR_STATUS_ID = "errorStatus";
	@Value("${nonExistentPageUrl}")
	private String nonExistentPageUrl;
	@Value("${simulateFilterErrorUrl}")
	private String simulateFilterErrorUrl;
	@Value("${customErrorSimulationUrl}")
	private String customErrorSimulationUrl;

	public final LogbackTestFileAppender logbackTestFileAppender = new LogbackTestFileAppender(CustomApplicationErrorController.class.getName());

	@BeforeEach
	public void setUp() {
		logbackTestFileAppender.startLogsCapture();
	}

	@AfterEach
	public void tearDown() {
		logbackTestFileAppender.stopLogsCapture();
	}

	@Test
	public void shouldReturnGeneralErrorPageWith404MessageGivenNonExistentPageForHtmlClients() throws IOException {
		WebDriver webDriver = newWebDriver();
		webDriver.navigate().to(nonExistentPageUrl);
		String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
		assertEquals(nonExistentPageUrl, webDriver.getCurrentUrl());
		assertEquals("404 Not Found", errorStatus);

		EntityExchangeResult<String> response = restTestClientForHtml.method(HttpMethod.GET)
			.uri(nonExistentPageUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		logbackTestFileAppender.assertLogEntry("DEBUG Not Found");
	}

	@Test
	public void shouldReturnGeneralErrorPageWith500MessageGivenNonExistentPageForHtmlClients() throws IOException {
		WebDriver webDriver = newWebDriver();
		webDriver.navigate().to(simulateFilterErrorUrl);
		String errorStatus = webDriver.findElement(By.id(ERROR_STATUS_ID)).getText();
		assertEquals(simulateFilterErrorUrl, webDriver.getCurrentUrl());
		assertEquals("500 Internal Server Error", errorStatus);

		EntityExchangeResult<String> response = restTestClientForHtml.method(HttpMethod.GET)
			.uri(simulateFilterErrorUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		logbackTestFileAppender.assertLogEntry("ERROR Internal Server Error");
	}

	@Test
	public void shouldGetNotFoundMessageGivenNonExistentPageForHttpClients() throws Exception {
		EntityExchangeResult<String> response = restTestClient.method(HttpMethod.GET)
			.uri(nonExistentPageUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
		ErrorDetails errorDetails = objectMapper.readValue(response.getResponseBody(), ErrorDetails.class);
		assertEquals(HttpStatus.NOT_FOUND.value(), errorDetails.getStatus());
		assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorDetails.getError());
		logbackTestFileAppender.assertLogEntry("DEBUG Not Found");
	}

	@Test
	public void shouldGetInternalServerErrorMessageGivenNonExistentPageForHttpClients() throws Exception {
		EntityExchangeResult<String> response = restTestClient.method(HttpMethod.GET)
			.uri(simulateFilterErrorUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
		ErrorDetails errorDetails = objectMapper.readValue(response.getResponseBody(), ErrorDetails.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetails.getStatus());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorDetails.getError());
		logbackTestFileAppender.assertLogEntry("ERROR Internal Server Error");
	}

	@Test
	public void shouldHandleCustomExceptionTo405Status() throws Exception {
		EntityExchangeResult<String> response = restTestClient.method(HttpMethod.GET)
			.uri(customErrorSimulationUrl)
			.exchange()
			.returnResult(String.class);
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatus());
		ErrorDetails errorDetails = objectMapper.readValue(response.getResponseBody(), ErrorDetails.class);
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), errorDetails.getStatus());
		assertEquals("ERR123 :: Unique error", errorDetails.getError());
	}


	public static class ErrorDetails {
		@JsonProperty("status")
		private final Integer status;
		@JsonProperty("error")
		private final String error;

		@JsonCreator
		public ErrorDetails(
			@JsonProperty("status") Integer status,
			@JsonProperty("error") String error
		) {
			this.status = status;
			this.error = error;
		}

		public int getStatus() {
			return status;
		}

		public String getError() {
			return error;
		}
	}
}
