package com.harishkannarao.jdbc.repository;

import com.harishkannarao.jdbc.entity.Order;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends ListCrudRepository<Order, Long> {

	List<Order> findByCustomerId(UUID customerId);

	@Query("select * from orders o where o.customer_id in (:customerIds)")
	List<Order> findAllByCustomerIds(@Param("customerIds") List<UUID> customerIds);

	@Modifying
	@Query("delete from orders o where o.customer_id in (:customerIds)")
	void deleteAllByCustomerIdIn(@Param("customerIds") List<UUID> customerIds);
}
