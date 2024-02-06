package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Qualifier("myCustomerDao")
public class CustomerDao {

	private final JdbcClient jdbcClient;

	@Autowired
	public CustomerDao(@Qualifier("myJdbcClient") JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public List<Customer> getAllCustomers() {
		return jdbcClient
			.sql("SELECT * FROM customers")
			.query(Customer.class)
			.list();
	}

	public List<Customer> getCustomersByFirstName(String firstName) {
		return jdbcClient
			.sql("SELECT * FROM customers WHERE first_name = :first_name")
			.param("first_name", firstName)
			.query(Customer.class)
			.list();
	}

	public void createCustomer(CreateCustomerRequestDto createCustomerRequestDto) {
		jdbcClient.sql("INSERT INTO customers(first_name, last_name) VALUES (:firstName,:lastName)")
			.paramSource(createCustomerRequestDto)
			.update();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createCustomerInIsolation(CreateCustomerRequestDto createCustomerRequestDto) {
		createCustomer(createCustomerRequestDto);
	}

	public void deleteCustomer(Long id) {
		jdbcClient.sql("DELETE FROM customers where id = :id")
			.param("id", id)
			.update();
	}
}
