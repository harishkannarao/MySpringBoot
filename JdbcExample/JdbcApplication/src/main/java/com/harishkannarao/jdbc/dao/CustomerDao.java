package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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


	public Customer getCustomersById(long id) {
		return jdbcClient
			.sql("SELECT * FROM customers WHERE id = :id")
			.param("id", id)
			.query(Customer.class)
			.single();
	}

	public Customer createCustomer(CreateCustomerRequestDto createCustomerRequestDto) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("INSERT INTO customers(first_name, last_name) VALUES (:firstName,:lastName)")
			.paramSource(createCustomerRequestDto)
			.update(keyHolder);
		int id = (Integer) keyHolder.getKeyList().getFirst().get("id");
		return getCustomersById(id);
	}

	public Customer createCustomerWithReturn(CreateCustomerRequestDto createCustomerRequestDto) {
		return jdbcClient.sql("INSERT INTO customers(first_name, last_name) VALUES (:firstName,:lastName) RETURNING *")
			.paramSource(createCustomerRequestDto)
			.query(Customer.class)
			.single();
	}

	public Customer updateCustomerWithReturn(Customer customer) {
		return jdbcClient.sql("""
				UPDATE customers
				 SET first_name=:firstName,last_name=:lastName
				 WHERE id=:id RETURNING *
				""")
			.paramSource(customer)
			.query(Customer.class)
			.single();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createCustomerInIsolation(CreateCustomerRequestDto createCustomerRequestDto) {
		createCustomerWithReturn(createCustomerRequestDto);
	}

	public int deleteCustomer(Integer id) {
		return jdbcClient.sql("DELETE FROM customers where id = :id")
			.param("id", id)
			.update();
	}
}
