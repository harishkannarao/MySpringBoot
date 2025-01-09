package com.harishkannarao.jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harishkannarao.jdbc.dao.CustomerDao;
import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerRestControllerTest {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final CustomerDao customerDao = Mockito.mock(CustomerDao.class);

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders
			.standaloneSetup(new CustomersRestController(customerDao))
			.build();
	}

	@Test
	public void test_createCustomer() throws Exception {
		CreateCustomerRequestDto input = new CreateCustomerRequestDto(
			"first-name-" + UUID.randomUUID(),
			"last-name-" + UUID.randomUUID()
		);

		Customer expected = new Customer(
			2,
			input.firstName(),
			input.lastName()
		);

		String inputJson = OBJECT_MAPPER.writeValueAsString(input);

		when(customerDao.createCustomer(any(CreateCustomerRequestDto.class)))
			.thenReturn(expected);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
				.post("/customers")
				.content(inputJson)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			)
			.andReturn()
			.getResponse();

		assertThat(response.getStatus())
			.isEqualTo(200);

		Customer result = OBJECT_MAPPER.readValue(response.getContentAsString(), Customer.class);

		assertThat(result)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(expected);

		verify(customerDao)
			.createCustomer(assertArg(value ->
				assertThat(value)
					.usingRecursiveComparison()
					.ignoringCollectionOrder()
					.isEqualTo(input)));
	}

	@Test
	public void test_createCustomer_throwsError() throws Exception {
		CreateCustomerRequestDto input = new CreateCustomerRequestDto(
			"first-name-" + UUID.randomUUID(),
			"last-name-" + UUID.randomUUID()
		);

		Customer expected = new Customer(
			2,
			input.firstName(),
			input.lastName()
		);

		String inputJson = OBJECT_MAPPER.writeValueAsString(input);

		when(customerDao.createCustomer(any(CreateCustomerRequestDto.class)))
			.thenThrow(new RuntimeException("Artificial Error"));

		ServletException result = catchThrowableOfType(() ->
				mockMvc.perform(MockMvcRequestBuilders
					.post("/customers")
					.content(inputJson)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.accept(MediaType.APPLICATION_JSON_VALUE)
				),
			ServletException.class);

		assertThat(result.getRootCause())
			.isInstanceOf(RuntimeException.class);

		RuntimeException runtimeException = (RuntimeException) result.getRootCause();
		assertThat(runtimeException.getMessage()).isEqualTo("Artificial Error");

		verify(customerDao)
			.createCustomer(assertArg(value ->
				assertThat(value)
					.usingRecursiveComparison()
					.ignoringCollectionOrder()
					.isEqualTo(input)));
	}
}
