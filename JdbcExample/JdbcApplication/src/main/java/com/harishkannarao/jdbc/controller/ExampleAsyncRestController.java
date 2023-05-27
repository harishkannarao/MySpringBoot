package com.harishkannarao.jdbc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping(value = "/async", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class ExampleAsyncRestController {

    private final Logger logger = LoggerFactory.getLogger(ExampleAsyncRestController.class);
    private final Executor executor;

    @Autowired
    public ExampleAsyncRestController(
            @Qualifier("asyncTaskExecutor") Executor executor
    ) {
        this.executor = executor;
    }

    @RequestMapping(value = "fireAndForget", method = RequestMethod.POST)
    public ResponseEntity<Void> fireAndForget(
            @RequestBody List<Long> ids) {
        Optional.ofNullable(ids)
                .orElseGet(Collections::emptyList)
                .forEach(aLong ->
                        CompletableFuture
                                .runAsync(() -> sendForId(aLong), executor)
                                .whenComplete(((unused, throwable) ->
                                        logger.error(throwable.getMessage(), throwable)))
                );
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "executeAndWait", method = RequestMethod.POST)
    public ResponseEntity<Void> executeAndWait() {
        return ResponseEntity.noContent().build();
    }

    private void sendForId(Long id) {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (id % 2 == 0) {
            logger.info("Success for id: " + id);
        } else {
            throw new IllegalArgumentException("Invalid id: " + id);
        }
    }
}
