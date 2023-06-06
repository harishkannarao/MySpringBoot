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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .forEach(id ->
                        CompletableFuture
                                .runAsync(() -> sendForId(id), executor)
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
    public ResponseEntity<List<Long>> executeAndWait(
            @RequestAttribute(REQUEST_ID_KEY) String requestId,
            @RequestBody List<Long> values
    ) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        List<CompletableFuture<FutureResult>> futures = Optional.ofNullable(values)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(value ->
                        CompletableFuture
                                .supplyAsync(() -> calculateSquare(value), executor)
                                .orTimeout(4, TimeUnit.SECONDS)
                                .handle((result, throwable) -> Optional.ofNullable(throwable)
                                        .map(FutureResult::error)
                                        .orElseGet(() -> FutureResult.success(result)))
                )
                .toList();
        Stream<FutureResult> results = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApplyAsync(unused -> {
                    try {
                        MDC.setContextMap(contextMap);
                        logger.info("Retrieving all future values");
                        return futures.stream().map(CompletableFuture::join);
                    } finally {
                        MDC.clear();
                    }
                })
                .join();
        List<Long> squaredValues = results.map(futureResult -> {
                    Optional<Throwable> exception = futureResult.getException();
                    exception.ifPresent(throwable -> logger.error(throwable.getMessage(), throwable));
                    return futureResult.getResult().orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
        return ResponseEntity.ok()
                .header(REQUEST_ID_KEY, requestId)
                .body(squaredValues);
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

    private Long calculateSquare(Long value) {
        try {
            if (value == 1) {
                Thread.sleep(6000L);
            } else if (value == 2) {
                Thread.sleep(2000L);
            } else {
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Calculating for: {}", value);
        if (value == 0) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        return value * value;
    }

    public static class FutureResult {
        private final Long result;
        private final Throwable exception;

        private FutureResult(Long result, Throwable exception) {
            this.result = result;
            this.exception = exception;
        }

        public Optional<Long> getResult() {
            return Optional.ofNullable(result);
        }

        public Optional<Throwable> getException() {
            return Optional.ofNullable(exception);
        }

        public static FutureResult success(Long result) {
            return new FutureResult(result, null);
        }
        public static FutureResult error(Throwable exception) {
            return new FutureResult(null, exception);
        }
    }
}
