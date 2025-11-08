package com.harishkannarao.jdbc.dao.repository;

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

	@Query(value = "SELECT * FROM order_documents WHERE json_data->>:key = :build")
	List<OrderDocument> findByJsonAttribute(@Param("key") String key, @Param("build") String value);

	@Query(value = "SELECT * FROM order_documents WHERE inventory->>'productCode' = :productCode")
	List<OrderDocument> findByProductCode(@Param("productCode") String code);
}
