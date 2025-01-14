package com.harishkannarao.jdbc.repository;

public interface WithInsert<T> {
	<S extends T> S insert(S entity);
}
