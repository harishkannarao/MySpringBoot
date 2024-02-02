package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;

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

        CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto();
        String firstName = "testFirstName";
        String lastName = "testLastName";
        createCustomerRequestDto.setFirstName(firstName);
        createCustomerRequestDto.setLastName(lastName);

        HttpEntity<CreateCustomerRequestDto> createRequest = new HttpEntity<>(createCustomerRequestDto);
        try {
						// this endpoint creates the customer twice
						// 1. In default request transaction
						// 2. In isolated transaction
						// The customer created using default request transaction will not be persisted due to
					  // RuntimeException thrown in the request
						// On the customer created using isolated transaction will be persisted
            restClient
							.put()
							.uri(transactionsEndpointUrl)
							.retrieve()
							.toBodilessEntity();
            fail("should have thrown exception");
        } catch (RestClientException exception) {
            assertTrue(exception instanceof HttpServerErrorException);
            HttpServerErrorException httpServerErrorException = (HttpServerErrorException) exception;
            assertTrue(httpServerErrorException.getResponseBodyAsString().contains("Another Bang"));
        }

        Customer[] updatedCustomers = restClient
					.get()
					.uri(allCustomersEndpointUrl)
					.retrieve()
					.body(Customer[].class);
        assertNotNull(updatedCustomers);
        assertEquals(6, updatedCustomers.length);

        long checkCustomer = Arrays.stream(updatedCustomers)
                .filter(it -> it.getFirstName().equals(firstName) && it.getLastName().equals(lastName))
                .count();

        assertEquals(1, checkCustomer);
    }
}
