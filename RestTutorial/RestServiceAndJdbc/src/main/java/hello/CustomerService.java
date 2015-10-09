package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("myCustomerService")
public class CustomerService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers",
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        );
    }
}
