package hello;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomerRestControllerIT extends BaseIntegrationTestWithRestServiceAndJdbcApplication {
    public static final String allCustomersEndpointStringFormat = "http://localhost:%s/customers";

    private String getAllCustomersEndpointString() {
        return String.format(allCustomersEndpointStringFormat, port);
    }

    @Test
    public void getAllCustomers_shouldReturnAllCustomers_fromDatabase() {
        Customer[] customersArray = restTemplate.getForObject(getAllCustomersEndpointString(), Customer[].class);
        List<Customer> customers = Arrays.asList(customersArray);
        assertEquals(5, customers.size());
    }
}
