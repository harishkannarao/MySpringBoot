package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.FeatureToggleResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FeatureToggleIT extends BaseIntegration {

    private static final String FEATURE_TOGGLE_PROPERTY = "custom.feature.toggle";

    @Value("${featureToggleEndpointUrl}")
    private String featureToggleEndpointUrl;
    @Value("${custom.feature.toggle}")
    private String originalFeatureToggleValue;

    @Before
    public void setUp() throws Exception {
        resetFeatureToggleProperty();

    }

    @After
    public void tearDown() throws Exception {
        resetFeatureToggleProperty();

    }

    @Test
    public void shouldReturnFeatureToggleDefaultStatusAsTrue() throws Exception {
        ResponseEntity<String> response = testRestTemplate.exchange(featureToggleEndpointUrl, HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FeatureToggleResponse featureToggleResponse = objectMapper.readValue(response.getBody(), FeatureToggleResponse.class);
        assertTrue(featureToggleResponse.isEnabled());
    }

    @Test
    public void shouldReturnFeatureToggleStatusAsFalse() throws Exception {
        EnvironmentTestUtils.addEnvironment(configurableEnvironment, FEATURE_TOGGLE_PROPERTY+"=false");

        ResponseEntity<String> response = testRestTemplate.exchange(featureToggleEndpointUrl, HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FeatureToggleResponse featureToggleResponse = objectMapper.readValue(response.getBody(), FeatureToggleResponse.class);
        assertFalse(featureToggleResponse.isEnabled());

    }

    private void resetFeatureToggleProperty() {
        EnvironmentTestUtils.addEnvironment(configurableEnvironment, FEATURE_TOGGLE_PROPERTY+"="+originalFeatureToggleValue);
    }

}
