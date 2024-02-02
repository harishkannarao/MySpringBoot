package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.controller.HealthCheckRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("ConstantConditions")
public class HealthCheckRestControllerIT extends BaseIntegrationJdbc {
    @Value("${healthCheckEndpointUrl}")
    public String healthCheckEndpointUrl;

    @Test
    public void verify_health_check_endpoint() {
        ResponseEntity<HealthCheckRestController.HealthCheckResponse> response = restClient
					.get()
					.uri(healthCheckEndpointUrl)
					.retrieve()
					.toEntity(HealthCheckRestController.HealthCheckResponse.class);
        HealthCheckRestController.HealthCheckResponse entity = response.getBody();

        assertThat(response.getStatusCode().value(), equalTo(200));
        assertThat(entity.getStatus(), equalTo("UP"));
        assertThat(entity.getCommit(), not(emptyOrNullString()));
    }
}
