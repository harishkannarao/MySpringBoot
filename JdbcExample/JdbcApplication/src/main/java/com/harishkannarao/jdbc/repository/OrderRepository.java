package com.harishkannarao.jdbc.repository;

import com.harishkannarao.jdbc.entity.Order;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository
	extends
	ListCrudRepository<Order, Long>,
	ListPagingAndSortingRepository<Order, Long> {

	List<Order> findByCustomerId(UUID customerId);

	@Modifying
	@Query("delete from orders o where o.customer_id in (:customerIds)")
	void deleteAllByCustomerIdIn(@Param("customerIds") List<UUID> customerIds);
}
