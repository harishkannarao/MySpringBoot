package com.harishkannarao.rest.controller;

import com.harishkannarao.rest.domain.FeatureToggleResponse;
import com.harishkannarao.rest.util.FeatureToggler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/featureToggle", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class FeatureToggleController {
    private final FeatureToggler featureToggler;

    @Autowired
    public FeatureToggleController(FeatureToggler featureToggler) {
        this.featureToggler = featureToggler;
    }

    @RequestMapping
    public ResponseEntity<FeatureToggleResponse> getFeatureToggleResponse() {
        FeatureToggleResponse featureToggleResponse = new FeatureToggleResponse(featureToggler.isCustomFeature());
        return new ResponseEntity<>(featureToggleResponse, HttpStatus.OK);
    }
}
