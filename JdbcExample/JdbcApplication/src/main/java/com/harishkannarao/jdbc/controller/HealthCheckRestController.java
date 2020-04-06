package com.harishkannarao.jdbc.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/health-check", produces = {MediaType.APPLICATION_JSON_VALUE})
public class HealthCheckRestController {

    @RequestMapping
    public ResponseEntity<HealthCheckResponse> getAllMenuEntries() {
        HealthCheckResponse entity = new HealthCheckResponse();
        entity.setStatus("UP");
        return ResponseEntity.ok(entity);
    }

    public static class HealthCheckResponse {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
