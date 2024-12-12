package com.harishkannarao.jdbc.domain;

public record CustomerBuilder(Customer build) {

	public static CustomerBuilder from(Customer value) {
		return new CustomerBuilder(value);
	}

	public CustomerBuilder firstName(String value) {
		return new CustomerBuilder(
			new Customer(build.id(), value, build().lastName()));
	}

	public CustomerBuilder lastName(String value) {
		return new CustomerBuilder(
			new Customer(build.id(), build().firstName(), value));
	}
}
