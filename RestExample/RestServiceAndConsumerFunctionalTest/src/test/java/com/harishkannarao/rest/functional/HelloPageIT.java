package com.harishkannarao.rest.functional;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.client.EntityExchangeResult;

import java.time.LocalDate;

import static com.harishkannarao.rest.filter.ResponseHeaderFilter.CUSTOM_HEADER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloPageIT extends BaseIntegration {

	@org.springframework.beans.factory.annotation.Value("${helloPageEndpointUrl}")
	public String helloPageEndpointUrl;

	@Test
	public void shouldGetIndexPage() {
		WebDriver webDriver = newWebDriver();
		webDriver.navigate().to(helloPageEndpointUrl);
		String date = webDriver.findElement(By.id("date")).getText();
		String message = webDriver.findElement(By.id("message")).getText();

		assertEquals(LocalDate.now().toString(), date);
		assertEquals("Hello Harish", message);
	}

	@Test
	public void shouldGetCustomHeaderInResponseGivenACustomHeaderIsPassedInTheRequest() throws Exception {
		String customHeaderValue = "someValue";
		EntityExchangeResult<String> response = restTestClientForHtml.method(HttpMethod.GET)
			.uri(helloPageEndpointUrl)
			.header(CUSTOM_HEADER_NAME, customHeaderValue)
			.exchange()
			.returnResult(String.class);
		assertEquals(200, response.getStatus().value());
		assertEquals(customHeaderValue, response.getResponseHeaders().getFirst(CUSTOM_HEADER_NAME));
	}
}
