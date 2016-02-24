package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomerRestControllerIT extends BaseIntegrationJdbc {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${allCustomersEndpointStringFormat}")
    public String allCustomersEndpointStringFormat;

    @Test
    public void getAllCustomers_shouldReturnAllCustomers_fromDatabase() {
        Customer[] customersArray = restTemplate.getForObject(allCustomersEndpointStringFormat, Customer[].class);
        List<Customer> customers = Arrays.asList(customersArray);
        assertEquals(5, customers.size());
    }
}
