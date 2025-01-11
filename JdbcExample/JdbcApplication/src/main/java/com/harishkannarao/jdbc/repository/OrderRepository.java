package com.harishkannarao.jdbc.repository;

import com.harishkannarao.jdbc.entity.Order;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface OrderRepository
	extends
	ListCrudRepository<Order, Long>,
	ListPagingAndSortingRepository<Order, Long> {
}
