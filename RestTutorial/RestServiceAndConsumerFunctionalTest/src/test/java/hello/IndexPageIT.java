package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class IndexPageIT extends BaseIntegration {

    @Autowired
    @org.springframework.beans.factory.annotation.Value("${indexPageEndpointUrl}")
    public String indexPageEndpointUrl;

    @Test
    public void shouldGetIndexPage() {
        String pageContent = htmlRestTemplate.getForObject(indexPageEndpointUrl, String.class);
        assertEquals("<b>Hello World !!!</b>", pageContent);
    }
}
