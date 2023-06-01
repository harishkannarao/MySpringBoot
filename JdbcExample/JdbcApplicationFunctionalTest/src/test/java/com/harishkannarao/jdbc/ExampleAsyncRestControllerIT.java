package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.client.interceptor.RestTemplateAccessLoggingInterceptor;
import com.harishkannarao.jdbc.controller.ExampleAsyncRestController;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
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
    private final LogbackTestAppender logbackTestAppender = new LogbackTestAppender(ExampleAsyncRestController.class.getName());

    @Autowired
    public ExampleAsyncRestControllerIT(
            @Value("${fireAndForgetEndpointUrl}") String fireAndForgetEndpointUrl) {
        this.fireAndForgetEndpointUrl = fireAndForgetEndpointUrl;
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
        ResponseEntity<Void> response = restTemplate.exchange(fireAndForgetEndpointUrl, HttpMethod.POST, requestEntity, Void.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);

        await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> {
                    assertThat(logbackTestAppender.getLogs())
                            .as("LogFile: " + logbackTestAppender.getLogFile())
                            .anySatisfy(s -> assertThat(s).contains("Success for id: 2"))
                            .anySatisfy(s -> assertThat(s).contains("Invalid id: 3"))
                            .anySatisfy(s -> assertThat(s).contains("Success for id: 4"))
                            .anySatisfy(s -> assertThat(s).contains("java.util.concurrent.TimeoutException"));

                });
    }
}
