package com.harishkannarao.jdbc.dao;

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
			.sql("SELECT id, first_name, last_name FROM customers")
			.query(Customer.class)
			.list();
	}

	public List<Customer> getCustomersByFirstName(String firstName) {
		return jdbcClient
			.sql("SELECT id, first_name, last_name FROM customers WHERE first_name = :first_name")
			.param("first_name", firstName)
			.query(Customer.class)
			.list();
	}

	public void createCustomer(String firstName, String lastName) {
		jdbcClient.sql("INSERT INTO customers(first_name, last_name) VALUES (:first_name,:last_name)")
			.param("first_name", firstName)
			.param("last_name", lastName)
			.update();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createCustomerInIsolation(String firstName, String lastName) {
		createCustomer(firstName, lastName);
	}

	public void deleteCustomer(Long id) {
		jdbcClient.sql("DELETE FROM customers where id = :id")
			.param("id", id)
			.update();
	}
}
