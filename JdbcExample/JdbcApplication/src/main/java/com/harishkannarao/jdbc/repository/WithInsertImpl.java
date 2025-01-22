package com.harishkannarao.jdbc.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import java.util.List;

public class WithInsertImpl<T> implements WithInsert<T> {

	private final JdbcAggregateTemplate jdbcAggregateTemplate;

	@Autowired
	public WithInsertImpl(JdbcAggregateTemplate jdbcAggregateTemplate) {
		this.jdbcAggregateTemplate = jdbcAggregateTemplate;
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
	public <S extends T> S upsert(S entity) {
		try {
			return jdbcAggregateTemplate.update(entity);
		} catch (DbActionExecutionException e) {
			return jdbcAggregateTemplate.insert(entity);
		}
	}
}
