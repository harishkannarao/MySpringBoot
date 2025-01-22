package com.harishkannarao.jdbc.repository;

import java.util.List;

public interface WithInsert<T> {
	<S extends T> S insert(S entity);
	List<T> insertAll(Iterable<T> entities);
	<S extends T> S save(S entity);
}
