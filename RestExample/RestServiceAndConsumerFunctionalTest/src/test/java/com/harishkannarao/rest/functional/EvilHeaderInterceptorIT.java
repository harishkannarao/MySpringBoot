package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.ErrorResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor.EVIL_HEADER_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class EvilHeaderInterceptorIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}")
    public String greetingEndpointUrl;
    @org.springframework.beans.factory.annotation.Value("${helloPageEndpointUrl}")
    public String helloPageEndpointUrl;


    @Test
    public void shouldGet400StatusWithDescriptionForHttpClients() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.add(EVIL_HEADER_NAME, "Something");
        String requestUrl = fromHttpUrl(greetingEndpointUrl).queryParam("name", "Harish").toUriString();
        HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(400, response.getStatusCode().value());
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        assertEquals("You are an evil request::/rest-service/greeting/get?name=Harish", errorResponse.getDescription());
    }


    @Test
    public void shouldGet400StatusWithDescriptionForHtmlClients() {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.add(EVIL_HEADER_NAME, "Something");
        HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> response = testRestTemplateForHtml.exchange(helloPageEndpointUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody(), containsString("You are an evil request::/rest-service/hello"));
        assertThat(response.getBody(), containsString("Something went wrong in controller:"));
    }

}
