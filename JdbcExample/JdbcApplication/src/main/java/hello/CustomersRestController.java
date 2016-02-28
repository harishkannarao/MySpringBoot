package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/customers", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomersRestController {

    private CustomerService customerService;

    @Autowired
    public CustomersRestController(@Qualifier("myCustomerService")CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}
