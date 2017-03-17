package com.harishkannarao.rest.functional;

import com.harishkannarao.rest.domain.ErrorResponse;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.harishkannarao.rest.interceptor.request.EvilHeaderRequestInterceptor.EVIL_HEADER_NAME;
import static org.junit.Assert.assertEquals;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class EvilHeaderInterceptorIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${greetingEndpointUrl}")
    public String greetingEndpointUrl;

    @Test
    public void shouldGet400StatusWithDescription() throws Exception {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.add(EVIL_HEADER_NAME, "Something");
        String requestUrl = fromHttpUrl(greetingEndpointUrl).queryParam("name", "Harish").toUriString();
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(400, response.getStatusCodeValue());
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
        assertEquals("You are an evil request::/greeting/get?name=Harish", errorResponse.getDescription());
    }

}
