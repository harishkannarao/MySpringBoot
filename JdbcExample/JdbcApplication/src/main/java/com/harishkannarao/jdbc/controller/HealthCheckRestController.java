package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.util.GitPropertiesUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"${application.healthCheck.path}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class HealthCheckRestController {

    @GetMapping
    public ResponseEntity<HealthCheckResponse> getHealthCheckResponse() {
        HealthCheckResponse entity = new HealthCheckResponse();
        entity.setStatus("UP");
        entity.setCommit(GitPropertiesUtil.getInstance().getGitCommitIdAbbrev());
        return ResponseEntity.ok(entity);
    }

    public static class HealthCheckResponse {
        private String status;
        private String commit;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCommit() {
            return commit;
        }

        public void setCommit(String commit) {
            this.commit = commit;
        }
    }
}
