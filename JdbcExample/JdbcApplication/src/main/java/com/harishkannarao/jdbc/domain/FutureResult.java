package com.harishkannarao.jdbc.domain;

import java.util.Optional;

public class FutureResult<T, R> {
	private final T input;
	private final R result;
	private final Throwable exception;

	public FutureResult(T input, R result, Throwable exception) {
		this.input = input;
		this.result = result;
		this.exception = exception;
	}

	public Optional<R> getResult() {
		return Optional.ofNullable(result);
	}

	public Optional<Throwable> getException() {
		return Optional.ofNullable(exception);
	}

	public T getInput() {
		return input;
	}
}
