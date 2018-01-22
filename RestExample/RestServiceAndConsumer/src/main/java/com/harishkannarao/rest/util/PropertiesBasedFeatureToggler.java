package com.harishkannarao.rest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesBasedFeatureToggler implements FeatureToggler {

    private final boolean customFeature;

    @Autowired
    public PropertiesBasedFeatureToggler(
            @Value("${custom.feature.toggle}") boolean customFeature
    ) {
        this.customFeature = customFeature;
    }

    @Override
    public boolean isCustomFeature() {
        return customFeature;
    }
}
