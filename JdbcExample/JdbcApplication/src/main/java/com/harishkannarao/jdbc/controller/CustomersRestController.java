package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/customers", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomersRestController {

    private CustomerDao customerDao;

    @Autowired
    public CustomersRestController(@Qualifier("myCustomerDao")CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @RequestMapping
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
}
