package com.harishkannarao.properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class DefaultPropertiesIT extends BaseIntegrationWithDefaultProperties {

    @Value("${properties.endpoint.url}")
    private String propertiesEndpointUrl;

    @Test
    public void shouldGetDefaultPropertyValues() {
        ResponseEntity<CustomProperties> response = testRestTemplate.getForEntity(propertiesEndpointUrl, CustomProperties.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("value1", response.getBody().getProperty1());
        assertEquals("value2", response.getBody().getProperty2());
    }
}
