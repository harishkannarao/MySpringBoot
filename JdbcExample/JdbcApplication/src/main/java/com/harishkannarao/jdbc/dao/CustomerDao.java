package com.harishkannarao.jdbc.dao;

import com.harishkannarao.jdbc.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("myCustomerDao")
public class CustomerDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CustomerDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @SuppressWarnings("CodeBlock2Expr")
    public List<Customer> getAllCustomers() {
        RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
            return toCustomer(rs);
        };
        return namedParameterJdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers",
                customerRowMapper
        );
    }

    @SuppressWarnings("CodeBlock2Expr")
    public List<Customer> getCustomersByFirstName(String firstName) {
        Map<String, Object> params = Map.ofEntries(
                Map.entry("first_name", firstName)
        );
        RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
            return toCustomer(rs);
        };
        return namedParameterJdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = :first_name",
                params,
                customerRowMapper
        );
    }

    public void createCustomer(String firstName, String lastName) {
        Map<String, Object> params = Map.ofEntries(
                Map.entry("first_name", firstName),
                Map.entry("last_name", lastName)
        );
        namedParameterJdbcTemplate.update("INSERT INTO customers(first_name, last_name) VALUES (:first_name,:last_name)", params);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createCustomerInIsolation(String firstName, String lastName) {
        createCustomer(firstName, lastName);
    }

    public void deleteCustomer(Long id) {
        Map<String, Object> params = Map.ofEntries(
                Map.entry("id", id)
        );
        namedParameterJdbcTemplate.update("DELETE FROM customers where id = :id", params);
    }

    private Customer toCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}
