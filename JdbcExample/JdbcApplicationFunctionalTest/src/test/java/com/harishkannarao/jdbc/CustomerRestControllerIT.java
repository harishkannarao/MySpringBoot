package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.DeleteCustomerRequestDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CustomerRestControllerIT extends BaseIntegrationJdbc {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${allCustomersEndpointUrl}")
    public String allCustomersEndpointUrl;

    @Test
    public void getAllCustomers_shouldReturnAllCustomers_fromDatabase() {
        Customer[] customersArray = restTemplate.getForObject(allCustomersEndpointUrl, Customer[].class);
        assertNotNull(customersArray);
        assertEquals(5, customersArray.length);
    }

    @Test
    public void canCreateAndDeleteCustomer() {
        Customer[] customersArray = restTemplate.getForObject(allCustomersEndpointUrl, Customer[].class);
        assertNotNull(customersArray);
        assertEquals(5, customersArray.length);

        CreateCustomerRequestDto createCustomerRequestDto = new CreateCustomerRequestDto();
        String firstName = "testFirstName";
        String lastName = "testLastName";
        createCustomerRequestDto.setFirstName(firstName);
        createCustomerRequestDto.setLastName(lastName);

        HttpEntity<CreateCustomerRequestDto> createRequest = new HttpEntity<>(createCustomerRequestDto);
        ResponseEntity<Void> createCustomerResponse = restTemplate.exchange(allCustomersEndpointUrl, HttpMethod.POST, createRequest, Void.class);

        assertEquals(200, createCustomerResponse.getStatusCodeValue());

        Customer[] updatedListAfterCreate = restTemplate.getForObject(allCustomersEndpointUrl, Customer[].class);
        assertNotNull(updatedListAfterCreate);
        assertEquals(6, updatedListAfterCreate.length);

        Optional<Customer> foundCustomer = Arrays.stream(updatedListAfterCreate)
                .filter(it -> it.getFirstName().equals(firstName) && it.getLastName().equals(lastName))
                .findFirst();

        assertTrue(foundCustomer.isPresent());

        Customer createdCustomer = foundCustomer.get();

        DeleteCustomerRequestDto deleteCustomerRequestDto = new DeleteCustomerRequestDto();
        deleteCustomerRequestDto.setId(createdCustomer.getId());

        HttpEntity<DeleteCustomerRequestDto> deleteRequest = new HttpEntity<>(deleteCustomerRequestDto);
        ResponseEntity<Void> deleteCustomerResponse = restTemplate.exchange(allCustomersEndpointUrl, HttpMethod.DELETE, deleteRequest, Void.class);

        assertEquals(200, deleteCustomerResponse.getStatusCodeValue());

        Customer[] updatedListAfterDelete = restTemplate.getForObject(allCustomersEndpointUrl, Customer[].class);
        assertNotNull(updatedListAfterDelete);
        assertEquals(5, updatedListAfterDelete.length);

        long checkCustomer = Arrays.stream(updatedListAfterDelete)
                .filter(it -> it.getFirstName().equals(firstName) && it.getLastName().equals(lastName))
                .count();

        assertEquals(0, checkCustomer);
    }
}
