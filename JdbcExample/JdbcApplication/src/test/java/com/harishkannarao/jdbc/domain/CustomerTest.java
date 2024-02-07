package com.harishkannarao.jdbc.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CustomerTest {

    @Test
    public void testCustomer() {
        Customer customer = new Customer(2, "test-first-name", "test-last-name");
        assertThat(customer.id(), equalTo(2));
        assertThat(customer.firstName(), equalTo("test-first-name"));
        assertThat(customer.lastName(), equalTo("test-last-name"));
    }
}
