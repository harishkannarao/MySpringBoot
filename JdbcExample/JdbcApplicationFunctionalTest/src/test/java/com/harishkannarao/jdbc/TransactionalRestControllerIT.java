package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionalRestControllerIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${transactionsEndpointUrl}")
	private String transactionsEndpointUrl;
	@Autowired
	@Value("${allCustomersEndpointUrl}")
	private String allCustomersEndpointUrl;


	@Test
	public void testTransaction() {
		Customer[] initialCustomers = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(initialCustomers);
		assertEquals(5, initialCustomers.length);

		String firstName = "testFirstName";
		String lastName = "testLastName";
		CreateCustomerRequestDto createCustomerRequestDto =
			new CreateCustomerRequestDto(firstName, lastName);

		// this endpoint creates the customer twice
		// 1. In default request transaction
		// 2. In isolated transaction
		// The customer created using default request transaction will not be persisted due to
		// RuntimeException thrown in the request
		// On the customer created using isolated transaction will be persisted
		RestClientException result = assertThrows(RestClientException.class, () -> restClient
			.put()
			.uri(transactionsEndpointUrl)
			.body(createCustomerRequestDto)
			.retrieve()
			.toBodilessEntity());

		assertThat(result).isInstanceOf(HttpServerErrorException.class);
		HttpServerErrorException httpServerErrorException = (HttpServerErrorException) result;
		assertThat(httpServerErrorException.getResponseBodyAsString()).contains("Another Bang");

		Customer[] updatedCustomers = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(updatedCustomers);
		assertEquals(6, updatedCustomers.length);

		long checkCustomer = Arrays.stream(updatedCustomers)
			.filter(it -> it.firstName().equals(firstName) && it.lastName().equals(lastName))
			.count();

		assertEquals(1, checkCustomer);
	}
}
