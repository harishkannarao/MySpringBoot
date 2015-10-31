package hello;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MenuEntriesRestControllerIT extends BaseIntegrationJdbc {
    @Autowired
    @org.springframework.beans.factory.annotation.Value("${allMenuEntriesEndpointStringFormat}")
    public String allMenuEntriesEndpointStringFormat;

    private String getAllMenuEntriesEndpointString() {
        return String.format(allMenuEntriesEndpointStringFormat, port);
    }

    @Test
    public void getAllMenuEntries_shouldReturnAllMenuEntries_fromDatabase() {
        String[] menuEntriesArray = restTemplate.getForObject(getAllMenuEntriesEndpointString(), String[].class);
        List<String> menuEntries = Arrays.asList(menuEntriesArray);
        assertEquals(3, menuEntries.size());
    }
}
