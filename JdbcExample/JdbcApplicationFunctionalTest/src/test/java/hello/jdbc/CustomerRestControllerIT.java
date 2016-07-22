package hello.jdbc;

import hello.jdbc.domain.Customer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomerRestControllerIT extends BaseIntegrationJdbc {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${allCustomersEndpointUrl}")
    public String allCustomersEndpointUrl;

    @Test
    public void getAllCustomers_shouldReturnAllCustomers_fromDatabase() {
        Customer[] customersArray = restTemplate.getForObject(allCustomersEndpointUrl, Customer[].class);
        List<Customer> customers = Arrays.asList(customersArray);
        assertEquals(5, customers.size());
    }
}
