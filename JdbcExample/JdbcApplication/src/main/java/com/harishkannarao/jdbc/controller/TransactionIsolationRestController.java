package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.dao.CustomerDao;
import com.harishkannarao.jdbc.domain.CreateCustomerRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transaction/customers", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class TransactionIsolationRestController extends AbstractBaseController {

    private final CustomerDao customerDao;

    @Autowired
    public TransactionIsolationRestController(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> createCustomerWithIsolatedTransaction(
            @RequestBody CreateCustomerRequestDto requestDto
    ) {
        customerDao.createCustomer(requestDto.getFirstName(), requestDto.getLastName());
        customerDao.createCustomerInIsolation(requestDto.getFirstName(), requestDto.getLastName());
        throw new RuntimeException("Another Bang");
    }
}
