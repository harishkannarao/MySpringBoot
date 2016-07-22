package hello;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import static hello.EvilHeaderRequestInterceptor.EVIL_HEADER_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
        try {
            restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, String.class);
            fail("Should throw exception");
        } catch (HttpClientErrorException hcee) {
            assertEquals(400, hcee.getStatusCode().value());
            ErrorResponse errorResponse = objectMapper.readValue(hcee.getResponseBodyAsString(), ErrorResponse.class);
            assertEquals("You are an evil request::/greeting/get?name=Harish", errorResponse.getDescription());
        }
    }

}
