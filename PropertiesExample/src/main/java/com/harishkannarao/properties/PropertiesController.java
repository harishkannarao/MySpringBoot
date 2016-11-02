package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PropertiesController {

    private final CustomProperties customProperties;

    @Autowired
    public PropertiesController(@Value("${custom.property1}") String property1, @Value("${custom.property2}") String property2) {
        CustomProperties customProperties = new CustomProperties();
        customProperties.setProperty1(property1);
        customProperties.setProperty2(property2);
        this.customProperties = customProperties;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public CustomProperties getCustomProperties() {
        return customProperties;
    }

}
