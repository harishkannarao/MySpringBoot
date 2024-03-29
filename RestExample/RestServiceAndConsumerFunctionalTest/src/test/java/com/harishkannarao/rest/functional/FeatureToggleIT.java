package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.FeatureToggleResponse;
import com.harishkannarao.rest.util.PropertiesBasedFeatureToggler;
import com.harishkannarao.rest.util.TestFeatureToggler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


public class FeatureToggleIT extends BaseIntegration {

    @Value("${featureToggleEndpointUrl}")
    private String featureToggleEndpointUrl;
    @Autowired
    private TestFeatureToggler testFeatureToggler;
    @Autowired
    private PropertiesBasedFeatureToggler propertiesBasedFeatureToggler;

    @BeforeEach
    public void setUp() throws Exception {
        testFeatureToggler.resetCustomFeature();
    }

    @AfterEach
    public void tearDown() throws Exception {
        testFeatureToggler.resetCustomFeature();
    }

    @Test
    public void shouldReturnFeatureToggleDefaultStatusAsTrue() throws Exception {
        testFeatureToggler.setCustomFeature(true);

        ResponseEntity<String> response = testRestTemplate.exchange(featureToggleEndpointUrl, HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FeatureToggleResponse featureToggleResponse = objectMapper.readValue(response.getBody(), FeatureToggleResponse.class);
        assertTrue(featureToggleResponse.isEnabled());
    }

    @Test
    public void shouldReturnFeatureToggleStatusAsFalse() throws Exception {
        testFeatureToggler.setCustomFeature(false);

        ResponseEntity<String> response = testRestTemplate.exchange(featureToggleEndpointUrl, HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FeatureToggleResponse featureToggleResponse = objectMapper.readValue(response.getBody(), FeatureToggleResponse.class);
        assertFalse(featureToggleResponse.isEnabled());

    }

    @Test
    public void shouldReadValueFromProperties() {
        assertTrue(propertiesBasedFeatureToggler.isCustomFeature());
    }

}
