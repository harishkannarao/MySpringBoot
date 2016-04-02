package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "properties", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class WarPropertiesController {

    private final WarProperties warProperties;

    @Autowired
    public WarPropertiesController(@Value("${custom.message}") String message) {
        WarProperties warProperties = new WarProperties();
        warProperties.setMessage(message);
        this.warProperties = warProperties;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public WarProperties getWarProperties() {
        return warProperties;
    }

}
