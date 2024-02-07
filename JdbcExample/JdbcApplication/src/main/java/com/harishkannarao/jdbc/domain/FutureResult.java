package com.harishkannarao.jdbc.domain;

import java.util.Optional;

public record FutureResult<T, R>(
	Optional<T> input,
	Optional<R> result,
	Optional<Throwable> exception
) {

}
