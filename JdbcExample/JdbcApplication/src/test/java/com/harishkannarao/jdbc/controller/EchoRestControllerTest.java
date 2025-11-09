package com.harishkannarao.jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.jdbc.domain.DeeplyImmutableRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class EchoRestControllerTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
		.findAndRegisterModules();

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final EchoRestController echoRestController = new EchoRestController();
	private final MockMvc mockMvc = MockMvcBuilders
		.standaloneSetup(echoRestController)
		.build();

	@Test
	public void test_echo_with_all_fields() throws Exception {
		final DeeplyImmutableRecord input = new DeeplyImmutableRecord(
			2,
			"text-value",
			Optional.of("optional-text-value"),
			List.of("value1", "value2"),
			Set.of("set-value-1", "set-value-2"),
			Map.of("key", "test-value"));

		final String inputJson = OBJECT_MAPPER.writeValueAsString(input);
		logger.info(inputJson);
		final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/echo")
				.content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getStatus())
			.isEqualTo(200);

		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		final DeeplyImmutableRecord result = OBJECT_MAPPER.readValue(responseJson, DeeplyImmutableRecord.class);

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}

	@Test
	public void test_echo_with_mandatory_fields() throws Exception {
		final DeeplyImmutableRecord input = new DeeplyImmutableRecord(
			2,
			"text-value",
			null,
			null,
			null,
			null);

		final String inputJson = OBJECT_MAPPER.writeValueAsString(input);
		logger.info(inputJson);
		final MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/echo")
				.content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andReturn()
			.getResponse();

		assertThat(response.getStatus())
			.isEqualTo(200);

		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		final DeeplyImmutableRecord result = OBJECT_MAPPER.readValue(responseJson, DeeplyImmutableRecord.class);

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(input);
	}
}
