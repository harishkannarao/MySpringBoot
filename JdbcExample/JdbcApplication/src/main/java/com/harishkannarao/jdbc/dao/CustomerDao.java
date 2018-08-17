package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Qualifier("myCustomerDao")
public class CustomerDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers",
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        );
    }

    public void createCustomer(String firstName, String lastName) {
        jdbcTemplate.update("INSERT INTO customers(first_name, last_name) VALUES (?,?)", firstName, lastName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createCustomerInIsolation(String firstName, String lastName) {
        createCustomer(firstName, lastName);
    }

    public void deleteCustomer(Long id) {
        jdbcTemplate.update("DELETE FROM customers where id = ?", id);
    }
}
