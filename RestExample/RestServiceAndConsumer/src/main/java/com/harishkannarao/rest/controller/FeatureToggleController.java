package com.harishkannarao.rest.controller;

import com.harishkannarao.rest.domain.FeatureToggleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/featureToggle", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class FeatureToggleController {

    private static final String CUSTOM_FEATURE_TOGGLE_PROPERTY = "custom.feature.toggle";
    private final Environment environment;

    @Autowired
    public FeatureToggleController(Environment environment) {
        this.environment = environment;
    }

    @RequestMapping
    public ResponseEntity<FeatureToggleResponse> getFeatureToggleResponse() {
        return new ResponseEntity<>(
                new FeatureToggleResponse(
                        Boolean.valueOf(
                                environment.getRequiredProperty(CUSTOM_FEATURE_TOGGLE_PROPERTY)
                        )
                ),
                HttpStatus.OK);
    }
}
