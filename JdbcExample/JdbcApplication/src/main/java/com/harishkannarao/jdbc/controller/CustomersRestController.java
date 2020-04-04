package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.dao.CustomerDao;
import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import com.harishkannarao.jdbc.domain.Customer;
import com.harishkannarao.jdbc.domain.DeleteCustomerRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RestController
@RequestMapping(value = "/customers", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomersRestController extends AbstractBaseController {

    private final CustomerDao customerDao;

    @Autowired
    public CustomersRestController(@Qualifier("myCustomerDao") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getAllCustomers(
            @RequestParam("firstName") Optional<String> firstName
    ) {
        if (firstName.isPresent()) {
            return customerDao.getCustomersByFirstName(firstName.get());
        } else {
            return customerDao.getAllCustomers();
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> createCustomer(
            @RequestBody CreateCustomerRequestDto requestDto
    ) {
        customerDao.createCustomer(requestDto.getFirstName(), requestDto.getLastName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> createCustomerWithTransaction(
            @RequestBody CreateCustomerRequestDto requestDto
    ) {
        customerDao.createCustomer(requestDto.getFirstName(), requestDto.getLastName());
        throw new RuntimeException("Bang Bang");
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteCustomer(
            @RequestBody DeleteCustomerRequestDto requestDto
    ) {
        customerDao.deleteCustomer(requestDto.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
