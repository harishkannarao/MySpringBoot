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
    public WarPropertiesController(
            @Value("${custom.message1}") String message1,
            @Value("${custom.message2}") String message2,
            @Value("${custom.message3}") String message3,
            @Value("${custom.message4}") String message4,
            @Value("${custom.message5}") String message5
    ) {
        WarProperties warProperties = new WarProperties();
        warProperties.setMessage1(message1);
        warProperties.setMessage2(message2);
        warProperties.setMessage3(message3);
        warProperties.setMessage4(message4);
        warProperties.setMessage5(message5);
        this.warProperties = warProperties;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public WarProperties getWarProperties() {
        return warProperties;
    }

}
