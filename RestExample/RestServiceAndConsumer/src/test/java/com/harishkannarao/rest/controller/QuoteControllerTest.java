package com.harishkannarao.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.harishkannarao.rest.client.ThirdPartyRestQuoteClient;
import com.harishkannarao.rest.domain.Quote;
import com.harishkannarao.rest.domain.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class QuoteControllerTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
		.findAndRegisterModules();

	private final ThirdPartyRestQuoteClient thirdPartyRestQuoteClient = mock();
	private final QuoteController quoteController = new QuoteController(thirdPartyRestQuoteClient);

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
			.standaloneSetup(quoteController)
			.build();
	}

	@Test
	public void getQuote_fromThirdPartyClient() throws Exception {
		Quote quote = new Quote();
		quote.setType("test");
		Value value = new Value();
		value.setId(2L);
		value.setQuote("test-quote");
		quote.setValue(value);
		when(thirdPartyRestQuoteClient.getQuote()).thenReturn(quote);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.get("/quote")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);

		JsonNode json = OBJECT_MAPPER.readTree(response.getContentAsString());
		assertThat(json.getNodeType()).isEqualTo(JsonNodeType.OBJECT);
		assertThat(json.get("type").asText()).isEqualTo("test");
		assertThat(json.get("value").get("id").asLong()).isEqualTo(2L);
		assertThat(json.get("value").get("quote").asText()).isEqualTo("test-quote");
	}
}
