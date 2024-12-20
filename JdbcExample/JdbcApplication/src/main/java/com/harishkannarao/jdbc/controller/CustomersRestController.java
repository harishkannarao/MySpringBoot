package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.dao.CustomerDao;
import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.DeleteCustomerRequestDto;
import com.harishkannarao.jdbc.domain.DeleteCustomerResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RestController
@RequestMapping(value = "/customers", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomersRestController {

	private static final Logger logger = LoggerFactory.getLogger(CustomersRestController.class);

	private final CustomerDao customerDao;

	@Autowired
	public CustomersRestController(@Qualifier("myCustomerDao") CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> getAllCustomers(
		@RequestParam("firstName") Optional<String> firstName
	) {
		if (firstName.isPresent()) {
			return customerDao.getCustomersByFirstName(firstName.get());
		} else {
			return customerDao.getAllCustomers();
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	public ResponseEntity<Customer> createCustomer(
		@RequestBody CreateCustomerRequestDto requestDto
	) {
		var customer = customerDao.createCustomer(requestDto);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@Transactional
	public ResponseEntity<Void> createCustomerWithTransaction(
		@RequestBody CreateCustomerRequestDto requestDto
	) {
		customerDao.createCustomer(requestDto);
		throw new RuntimeException("Bang Bang");
	}

	@RequestMapping(method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DeleteCustomerResponseDto> deleteCustomer(
		@RequestBody DeleteCustomerRequestDto requestDto
	) {
		var responseEntity = new DeleteCustomerResponseDto(customerDao.deleteCustomer(requestDto.id()));
		return new ResponseEntity<>(responseEntity, HttpStatus.OK);
	}
}
