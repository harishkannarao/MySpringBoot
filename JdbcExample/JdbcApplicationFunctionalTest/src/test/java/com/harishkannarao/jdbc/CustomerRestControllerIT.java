package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.DeleteCustomerRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerRestControllerIT extends BaseIntegrationJdbc {
	@Autowired
	@Value("${allCustomersEndpointUrl}")
	private String allCustomersEndpointUrl;

	@Test
	public void getAllCustomers_shouldReturnAllCustomers_fromDatabase() {
		Customer[] customersArray = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(customersArray);
		assertEquals(5, customersArray.length);
	}

	@Test
	public void getAllCustomers_shouldReturn_customersWithMatchingName_fromDatabase() {
		URI uri = UriComponentsBuilder.fromHttpUrl(allCustomersEndpointUrl)
			.queryParam("firstName", "Josh")
			.build()
			.toUri();
		Customer[] customersArray = restClient
			.get()
			.uri(uri)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(customersArray);
		assertEquals(2, customersArray.length);
	}

	@Test
	public void canCreateAndDeleteCustomer() {
		Customer[] initialCustomers = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(initialCustomers);
		assertEquals(5, initialCustomers.length);

		CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto();
		String firstName = "testFirstName";
		String lastName = "testLastName";
		createCustomerRequestDto.setFirstName(firstName);
		createCustomerRequestDto.setLastName(lastName);

		ResponseEntity<Void> createCustomerResponse = restClient
			.post()
			.uri(allCustomersEndpointUrl)
			.body(createCustomerRequestDto)
			.retrieve()
			.toBodilessEntity();

		assertEquals(200, createCustomerResponse.getStatusCode().value());

		Customer[] updatedListAfterCreate = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(updatedListAfterCreate);
		assertEquals(6, updatedListAfterCreate.length);

		Optional<Customer> foundCustomer = Arrays.stream(updatedListAfterCreate)
			.filter(it -> it.getFirstName().equals(firstName) && it.getLastName().equals(lastName))
			.findFirst();

		assertTrue(foundCustomer.isPresent());

		Customer createdCustomer = foundCustomer.get();

		DeleteCustomerRequestDto deleteCustomerRequestDto = new DeleteCustomerRequestDto();
		deleteCustomerRequestDto.setId(createdCustomer.getId());

		ResponseEntity<Void> deleteCustomerResponse = restClient
			.method(HttpMethod.DELETE)
			.uri(allCustomersEndpointUrl)
			.body(deleteCustomerRequestDto)
			.retrieve()
			.toBodilessEntity();

		assertEquals(200, deleteCustomerResponse.getStatusCode().value());

		Customer[] updatedListAfterDelete = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(updatedListAfterDelete);
		assertEquals(5, updatedListAfterDelete.length);

		long checkCustomer = Arrays.stream(updatedListAfterDelete)
			.filter(it -> it.getFirstName().equals(firstName) && it.getLastName().equals(lastName))
			.count();

		assertEquals(0, checkCustomer);
	}

	@Test
	public void testTransaction() {
		Customer[] initialCustomers = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(initialCustomers);
		assertEquals(5, initialCustomers.length);

		CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto();
		String firstName = "testFirstName";
		String lastName = "testLastName";
		createCustomerRequestDto.setFirstName(firstName);
		createCustomerRequestDto.setLastName(lastName);

		RestClientException result = assertThrows(RestClientException.class, () -> restClient
			.put()
			.uri(allCustomersEndpointUrl)
			.body(createCustomerRequestDto)
			.retrieve()
			.toBodilessEntity());

		assertThat(result).isInstanceOf(HttpServerErrorException.class);
		HttpServerErrorException httpServerErrorException = (HttpServerErrorException) result;
		assertThat(httpServerErrorException.getResponseBodyAsString()).contains("Bang Bang");

		Customer[] updatedCustomers = restClient
			.get()
			.uri(allCustomersEndpointUrl)
			.retrieve()
			.body(Customer[].class);
		assertNotNull(updatedCustomers);
		assertEquals(5, updatedCustomers.length);

		long checkCustomer = Arrays.stream(updatedCustomers)
			.filter(it -> it.getFirstName().equals(firstName) && it.getLastName().equals(lastName))
			.count();

		assertEquals(0, checkCustomer);
	}
}
