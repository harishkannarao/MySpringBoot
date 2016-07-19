package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class HelloPageIT extends BaseIntegration {

    @org.springframework.beans.factory.annotation.Value("${helloPageEndpointUrl}")
    public String helloPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        String pageContent = htmlRestTemplate.getForObject(helloPageEndpointUrl, String.class);
        assertTrue(pageContent.contains("Message: Hello Harish"));
    }

}
