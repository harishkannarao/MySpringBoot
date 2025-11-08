package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.domain.FutureResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.harishkannarao.jdbc.filter.RequestTracingFilter.REQUEST_ID_KEY;
import static java.util.Optional.empty;
import static java.util.Optional.of;

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
					.runAsync(() -> runFireAndForget(contextMap, id), executor)
					.orTimeout(3, TimeUnit.SECONDS)
					.whenComplete(((unused, throwable) -> handleCompletion(contextMap, throwable)))
			);
		return ResponseEntity.noContent()
			.header(REQUEST_ID_KEY, requestId)
			.build();
	}

	@RequestMapping(value = "executeAndWait", method = RequestMethod.POST)
	public ResponseEntity<List<String>> executeAndWait(
		@RequestAttribute(REQUEST_ID_KEY) String requestId,
		@RequestBody List<Long> values
	) {
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		List<CompletableFuture<FutureResult<Long, Long>>> futures = Optional.ofNullable(values)
			.orElseGet(Collections::emptyList)
			.stream()
			.map(value ->
				CompletableFuture
					.supplyAsync(() -> runExecuteAndWait(contextMap, value), executor)
					.orTimeout(4, TimeUnit.SECONDS)
					.handle((result, throwable) -> handleResultAndException(contextMap, value, result, throwable))
			)
			.toList();
		Stream<FutureResult<Long, Long>> results = CompletableFuture
			.allOf(futures.toArray(CompletableFuture[]::new))
			.thenApplyAsync(unused -> joinAllFutureResult(contextMap, futures), executor)
			.join();
		List<String> result = results.map(futureResult -> {
				Optional<Throwable> exception = futureResult.exception();
				exception.ifPresent(throwable ->
					logger.error("Exception for input: " + futureResult.input().orElseThrow(), throwable));
				return futureResult.result()
					.map(aLong -> futureResult.input().orElseThrow() + "=" + aLong).orElse(null);
			})
			.filter(Objects::nonNull)
			.toList();
		return ResponseEntity.ok()
			.header(REQUEST_ID_KEY, requestId)
			.body(result);
	}

	private Stream<FutureResult<Long, Long>> joinAllFutureResult(Map<String, String> contextMap, List<CompletableFuture<FutureResult<Long, Long>>> futures) {
		try {
			MDC.setContextMap(contextMap);
			logger.info("Retrieving all future values");
			return futures.stream().map(CompletableFuture::join);
		} finally {
			MDC.clear();
		}
	}

	private static FutureResult<Long, Long> handleResultAndException(Map<String, String> contextMap, Long value, Long result, Throwable throwable) {
		try {
			MDC.setContextMap(contextMap);
			return Optional.ofNullable(throwable)
				.map(ex -> new FutureResult<Long, Long>(of(value), empty(), of(throwable)))
				.orElseGet(() -> new FutureResult<>(of(value), of(result), empty()));
		} finally {
			MDC.clear();
		}
	}

	private Long runExecuteAndWait(Map<String, String> contextMap, Long value) {
		try {
			MDC.setContextMap(contextMap);
			return calculateSquare(value);
		} finally {
			MDC.clear();
		}
	}

	private void runFireAndForget(Map<String, String> contextMap, Long id) {
		try {
			MDC.setContextMap(contextMap);
			sendForId(id);
		} finally {
			MDC.clear();
		}
	}

	private void handleCompletion(Map<String, String> contextMap, Throwable throwable) {
		if (Objects.nonNull(throwable)) {
			try {
				MDC.setContextMap(contextMap);
				logger.error(throwable.getMessage(), throwable);
			} finally {
				MDC.clear();
			}
		}
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
			throw new IllegalArgumentException("Invalid build: " + value);
		}
		return value * value;
	}
}
