package com.harishkannarao.jdbc.repository;

import com.harishkannarao.jdbc.entity.OrderDocument;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderDocumentRepository extends
	ListCrudRepository<OrderDocument, UUID>,
	WithInsert<OrderDocument> {

	List<OrderDocument> findByOrderIdIn(Set<Long> orderIds);
}
