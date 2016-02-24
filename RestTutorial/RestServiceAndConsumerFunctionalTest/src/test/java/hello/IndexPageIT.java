package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class IndexPageIT extends BaseIntegration {

    @Autowired
    @org.springframework.beans.factory.annotation.Value("${indexPageEndpointStringFormat}")
    public String indexPageEndpointStringFormat;

    @Test
    public void shouldGetIndexPage() {
        String pageContent = htmlRestTemplate.getForObject(indexPageEndpointStringFormat, String.class);
        assertEquals("<b>Hello World !!!</b>", pageContent);
    }
}
