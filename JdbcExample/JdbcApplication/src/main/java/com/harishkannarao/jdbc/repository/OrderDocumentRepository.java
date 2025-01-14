package com.harishkannarao.jdbc.repository;

import com.harishkannarao.jdbc.entity.OrderDocument;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OrderDocumentRepository extends
	ListCrudRepository<OrderDocument, UUID>,
	WithInsert<OrderDocument> {

	List<OrderDocument> findByOrderIdIn(Set<Long> orderIds);

	@Query(value = "SELECT * FROM order_documents WHERE data->>:key = :value")
	List<OrderDocument> findByJsonAttribute(@Param("key") String key, @Param("value") String value);
}
