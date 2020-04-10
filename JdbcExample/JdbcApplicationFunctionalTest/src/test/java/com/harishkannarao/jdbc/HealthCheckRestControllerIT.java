package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.controller.HealthCheckRestController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("ConstantConditions")
public class HealthCheckRestControllerIT extends BaseIntegrationJdbc {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${healthCheckEndpointUrl}")
    public String healthCheckEndpointUrl;

    @Test
    public void getAllMenuEntries_shouldReturnAllMenuEntries_fromDatabase() {
        ResponseEntity<HealthCheckRestController.HealthCheckResponse> response = restTemplate.exchange(healthCheckEndpointUrl, HttpMethod.GET, null, HealthCheckRestController.HealthCheckResponse.class);
        HealthCheckRestController.HealthCheckResponse entity = response.getBody();

        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(entity.getStatus(), equalTo("UP"));
        assertThat(entity.getCommit(), not(emptyOrNullString()));
    }
}
