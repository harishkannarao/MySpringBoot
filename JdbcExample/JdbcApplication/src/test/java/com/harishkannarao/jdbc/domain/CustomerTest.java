package com.harishkannarao.jdbc.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CustomerTest {

    @Test
    public void testCustomer() {
        Customer customer = new Customer(2L, "test-first-name", "test-last-name");
        assertThat(customer.getId(), equalTo(2L));
        assertThat(customer.getFirstName(), equalTo("test-first-name"));
        assertThat(customer.getLastName(), equalTo("test-last-name"));
    }
}
