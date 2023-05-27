package com.harishkannarao.jdbc.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/async", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class ExampleAsyncRestController {

    @RequestMapping(value = "fireAndForget", method = RequestMethod.POST)
    public ResponseEntity<Void> fireAndForget() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "executeAndWait", method = RequestMethod.POST)
    public ResponseEntity<Void> executeAndWait() {
        return ResponseEntity.noContent().build();
    }
}
