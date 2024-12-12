package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.dao.CustomerDao;
import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.CustomerBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerDaoIT extends BaseIntegrationJdbc {

	private final CustomerDao customerDao;

	@Autowired
	public CustomerDaoIT(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Test
	public void testInsertAndReturn() {
		CreateCustomerRequestDto input = new CreateCustomerRequestDto(
			"first-" + UUID.randomUUID(),
			"last-" + UUID.randomUUID());
		Customer result = customerDao.createCustomerWithReturn(input);

		assertThat(result).isNotNull();
		assertThat(result.id()).isGreaterThan(0);
		assertThat(result.firstName()).isEqualTo(input.firstName());
		assertThat(result.lastName()).isEqualTo(input.lastName());
	}

	@Test
	public void testUpdateAndReturn() {
		CreateCustomerRequestDto input = new CreateCustomerRequestDto(
			"first-" + UUID.randomUUID(),
			"last-" + UUID.randomUUID());
		Customer createdCustomer = customerDao.createCustomerWithReturn(input);

		Customer customerToUpdate = CustomerBuilder.from(createdCustomer)
			.firstName("f-" + UUID.randomUUID())
			.lastName("l-" + UUID.randomUUID())
			.build();

		Customer updatedCustomer = customerDao.updateCustomerWithReturn(customerToUpdate);

		assertThat(updatedCustomer)
			.usingRecursiveComparison()
			.ignoringCollectionOrder()
			.isEqualTo(customerToUpdate);

		assertThat(updatedCustomer.id())
			.isEqualTo(createdCustomer.id());
	}
}
