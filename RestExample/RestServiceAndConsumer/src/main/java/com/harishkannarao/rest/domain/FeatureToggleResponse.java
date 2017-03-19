package com.harishkannarao.rest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureToggleResponse {
    @JsonProperty("enabled")
    private final boolean enabled;

    @JsonCreator
    public FeatureToggleResponse(
            @JsonProperty("enabled") boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
