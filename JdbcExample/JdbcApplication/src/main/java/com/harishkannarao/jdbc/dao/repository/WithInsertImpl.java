package com.harishkannarao.jdbc.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;

public class WithInsertImpl<T> implements WithInsert<T> {

	private final JdbcAggregateTemplate jdbcAggregateTemplate;
	private final TransactionTemplate transactionTemplate;

	@Autowired
	public WithInsertImpl(
		JdbcAggregateTemplate jdbcAggregateTemplate,
		PlatformTransactionManager platformTransactionManager) {
		this.jdbcAggregateTemplate = jdbcAggregateTemplate;
		transactionTemplate = new TransactionTemplate(platformTransactionManager);
		transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
	}

	@Override
	public <S extends T> S insert(S entity) {
		return jdbcAggregateTemplate.insert(entity);
	}

	@Override
	public List<T> insertAll(Iterable<T> entities) {
		return jdbcAggregateTemplate.insertAll(entities);
	}

	@Override
	public <S extends T> S save(S entity) {
		S insertedEntity = transactionTemplate.execute(transactionStatus -> {
			try {
				return jdbcAggregateTemplate.insert(entity);
			} catch (DbActionExecutionException e) {
				if (e.getCause() instanceof DuplicateKeyException) {
					return null;
				}
				throw e;
			}
		});
		if (Objects.nonNull(insertedEntity)) {
			return insertedEntity;
		} else {
			return jdbcAggregateTemplate.update(entity);
		}
	}
}
