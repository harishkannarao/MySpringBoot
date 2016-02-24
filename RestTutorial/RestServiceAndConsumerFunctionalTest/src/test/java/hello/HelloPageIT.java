package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class HelloPageIT extends BaseIntegration {

    @Autowired
    @org.springframework.beans.factory.annotation.Value("${helloPageEndpointStringFormat}")
    public String helloPageEndpointStringFormat;

    @Test
    public void shouldGetIndexPage() {
        String pageContent = htmlRestTemplate.getForObject(helloPageEndpointStringFormat, String.class);
        assertTrue(pageContent.contains("Message: Hello Harish"));
    }

}
