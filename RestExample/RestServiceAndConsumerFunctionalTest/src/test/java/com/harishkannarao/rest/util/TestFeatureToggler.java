package com.harishkannarao.rest.util;

import java.util.Optional;

public class TestFeatureToggler implements FeatureToggler {

    private final FeatureToggler defaultFeatureToggler;

    private Boolean customFeature;

    public TestFeatureToggler(PropertiesBasedFeatureToggler propertiesBasedFeatureToggler) {
        this.defaultFeatureToggler = propertiesBasedFeatureToggler;
    }

    @Override
    public boolean isCustomFeature() {
        return Optional.ofNullable(customFeature)
                .orElseGet(defaultFeatureToggler::isCustomFeature);
    }

    public void setCustomFeature(boolean value) {
        customFeature = value;
    }

    public void resetCustomFeature() {
        customFeature = null;
    }
}
