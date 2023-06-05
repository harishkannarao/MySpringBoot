package com.harishkannarao.jdbc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.harishkannarao.jdbc.filter.RequestTracingFilter.REQUEST_ID_KEY;

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
            @RequestAttribute(REQUEST_ID_KEY) String requestId,
            @RequestBody List<Long> ids) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        Optional.ofNullable(ids)
                .orElseGet(Collections::emptyList)
                .forEach(aLong ->
                        CompletableFuture
                                .runAsync(() -> sendForId(aLong), executor)
                                .orTimeout(3, TimeUnit.SECONDS)
                                .whenComplete(((unused, throwable) -> {
                                    if (Objects.nonNull(throwable)) {
                                        try {
                                            MDC.setContextMap(contextMap);
                                            logger.error(throwable.getMessage(), throwable);
                                        } finally {
                                            MDC.clear();
                                        }
                                    }
                                }))
                );
        return ResponseEntity.noContent()
                .header(REQUEST_ID_KEY, requestId)
                .build();
    }

    @RequestMapping(value = "executeAndWait", method = RequestMethod.POST)
    public ResponseEntity<Void> executeAndWait() {
        return ResponseEntity.noContent().build();
    }

    private void sendForId(Long id) {
        try {
            if (id == 1) {
                Thread.sleep(4000L);
            } else {
                Thread.sleep(2000L);
            }
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
