package com.harishkannarao.jdbc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import com.harishkannarao.jdbc.controller.ExampleAsyncRestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class ExampleAsyncRestControllerIT extends BaseIntegrationJdbc {

    private final String fireAndForgetEndpointUrl;
    private final String executeAndWaitEndpointUrl;
    private final LogbackTestAppender logbackTestAppender = new LogbackTestAppender(ExampleAsyncRestController.class.getName(), Level.INFO);

    @Autowired
    public ExampleAsyncRestControllerIT(
            @Value("${fireAndForgetEndpointUrl}") String fireAndForgetEndpointUrl,
            @Value("${executeAndWaitEndpointUrl}") String executeAndWaitEndpointUrl) {
        this.fireAndForgetEndpointUrl = fireAndForgetEndpointUrl;
        this.executeAndWaitEndpointUrl = executeAndWaitEndpointUrl;
    }

    @BeforeEach
    public void setUp() {
        logbackTestAppender.startLogsCapture();
    }

    @AfterEach
    public void tearDown() {
        logbackTestAppender.stopLogsCapture();
    }

    @Test
    public void test_fireAndForget() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Integer> body = List.of(1,2,3,4);
        HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restClient
					.post()
					.uri(fireAndForgetEndpointUrl)
					.body(requestEntity)
					.retrieve()
					.toBodilessEntity();
        assertThat(response.getStatusCode().value()).isEqualTo(204);
        String requestId = response.getHeaders().getFirst("request_id");
        assertThat(requestId).isNotBlank();

        await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> assertThat(logbackTestAppender.getLogs())
                        .extracting(ILoggingEvent::getFormattedMessage)
                        .anySatisfy(s -> assertThat(s).contains("Success for id: 2"))
                        .anySatisfy(s -> assertThat(s).contains("Invalid id: 3"))
                        .anySatisfy(s -> assertThat(s).contains("Success for id: 4")));

        await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> assertThat(logbackTestAppender.getLogs())
                        .filteredOn(iLoggingEvent -> iLoggingEvent.getThrowableProxy() != null)
                        .extracting(ILoggingEvent::getThrowableProxy)
                        .extracting(IThrowableProxy::getClassName)
                        .anySatisfy(s -> assertThat(s).contains("java.util.concurrent.TimeoutException")));

        await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> assertThat(logbackTestAppender.getLogs())
                        .extracting(ILoggingEvent::getMDCPropertyMap)
                        .allSatisfy(mdc -> assertThat(mdc.get("request_id")).isEqualTo(requestId)));
    }


    @Test
    public void test_executeAndWait() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Long> body = List.of(0L,1L,2L,3L);
        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String[]> response = restClient
					.post()
					.uri(executeAndWaitEndpointUrl)
					.body(requestEntity)
					.retrieve()
					.toEntity(String[].class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .contains("2=4")
                .contains("3=9")
                .hasSize(2);
        String requestId = response.getHeaders().getFirst("request_id");
        assertThat(requestId).isNotBlank();

        await().atMost(Duration.ofSeconds(8))
                .untilAsserted(() -> assertThat(logbackTestAppender.getLogs())
                        .extracting(ILoggingEvent::getFormattedMessage)
                        .anySatisfy(s -> assertThat(s).contains("Calculating for: 0"))
                        .anySatisfy(s -> assertThat(s).contains("Calculating for: 1"))
                        .anySatisfy(s -> assertThat(s).contains("Calculating for: 2"))
                        .anySatisfy(s -> assertThat(s).contains("Calculating for: 3"))
                        .anySatisfy(s -> assertThat(s).contains("Exception for input: 0"))
                        .anySatisfy(s -> assertThat(s).contains("Exception for input: 1")));

        await().atMost(Duration.ofSeconds(8))
                .untilAsserted(() -> assertThat(logbackTestAppender.getLogs())
                        .extracting(ILoggingEvent::getMDCPropertyMap)
                        .allSatisfy(mdc -> assertThat(mdc.get("request_id")).isEqualTo(requestId)));
    }
}
