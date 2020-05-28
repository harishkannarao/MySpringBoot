package com.harishkannarao.jdbc;

import com.harishkannarao.jdbc.security.Subject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

@SuppressWarnings("ConstantConditions")
public class ExampleAuthRestControllerIT extends BaseIntegrationJdbc {
    @Value("${exampleAuthHeaderEndpointUrl}")
    private String exampleAuthHeaderEndpointUrl;
    @Value("${exampleAuthCookieEndpointUrl}")
    private String exampleAuthCookieEndpointUrl;

    @Test
    public void auth_header_should_return_valid_caller() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer HELLO_HEADER");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Subject> response = restTemplate.exchange(exampleAuthHeaderEndpointUrl, HttpMethod.GET, requestEntity, Subject.class);
        Subject entity = response.getBody();

        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(entity.getId(), equalTo("header-id"));
        assertThat(entity.getRoles(), containsInAnyOrder("header-role"));
    }

    @Test
    public void auth_header_should_return_401_for_missing_header() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(exampleAuthHeaderEndpointUrl, HttpMethod.GET, requestEntity, Void.class);
            fail("Should have thrown exception");
        } catch (HttpClientErrorException result) {
            assertThat(result.getRawStatusCode(), equalTo(401));
            assertThat(result.getMessage(), containsString("Authentication required"));
        }
    }

    @Test
    public void auth_header_should_return_401_for_invalid_header() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer invalid-value");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(exampleAuthHeaderEndpointUrl, HttpMethod.GET, requestEntity, Void.class);
            fail("Should have thrown exception");
        } catch (HttpClientErrorException result) {
            assertThat(result.getRawStatusCode(), equalTo(401));
            assertThat(result.getMessage(), containsString("Authentication required"));
        }
    }

    @Test
    public void auth_header_should_return_403_for_incorrect_role() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "session_cookie=HELLO_COOKIE;");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(exampleAuthHeaderEndpointUrl, HttpMethod.GET, requestEntity, Void.class);
            fail("Should have thrown exception");
        } catch (HttpClientErrorException result) {
            assertThat(result.getRawStatusCode(), equalTo(403));
            assertThat(result.getMessage(), containsString("Operation not permitted"));
        }
    }

    @Test
    public void auth_cookie_should_return_valid_caller() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "session_cookie=HELLO_COOKIE;");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Subject> response = restTemplate.exchange(exampleAuthCookieEndpointUrl, HttpMethod.GET, requestEntity, Subject.class);
        Subject entity = response.getBody();

        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(entity.getId(), equalTo("cookie-id"));
        assertThat(entity.getRoles(), containsInAnyOrder("cookie-role"));
    }

    @Test
    public void auth_cookie_should_return_401_for_missing_header() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(exampleAuthCookieEndpointUrl, HttpMethod.GET, requestEntity, Void.class);
            fail("Should have thrown exception");
        } catch (HttpClientErrorException result) {
            assertThat(result.getRawStatusCode(), equalTo(401));
            assertThat(result.getMessage(), containsString("Authentication required"));
        }
    }

    @Test
    public void auth_cookie_should_return_401_for_invalid_header() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "session_cookie=invalid_cookie;");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(exampleAuthCookieEndpointUrl, HttpMethod.GET, requestEntity, Void.class);
            fail("Should have thrown exception");
        } catch (HttpClientErrorException result) {
            assertThat(result.getRawStatusCode(), equalTo(401));
            assertThat(result.getMessage(), containsString("Authentication required"));
        }
    }

    @Test
    public void auth_cookie_should_return_403_for_incorrect_role() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer HELLO_HEADER");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(exampleAuthCookieEndpointUrl, HttpMethod.GET, requestEntity, Void.class);
            fail("Should have thrown exception");
        } catch (HttpClientErrorException result) {
            assertThat(result.getRawStatusCode(), equalTo(403));
            assertThat(result.getMessage(), containsString("Operation not permitted"));
        }
    }
}
