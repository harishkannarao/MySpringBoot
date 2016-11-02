package com.harishkannarao.war;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "configdata.js")
public class WarConfigDataController {

    private final Resource configDataTemplate = new ClassPathResource("/template/config_data_template.js");

    private final String configData;

    @Autowired
    public WarConfigDataController(@Value("${custom.message1}") String message1,
                                   @Value("${custom.message2}") String message2,
                                   @Value("${custom.message3}") String message3,
                                   @Value("${custom.message4}") String message4,
                                   @Value("${custom.message5}") String message5
    ) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(configDataTemplate.getURI()));
        String configDataTemplateString = new String(bytes);

        Map<String, String> data = new HashMap<>();
        data.put("message1", message1);
        data.put("message2", message2);
        data.put("message3", message3);
        data.put("message4", message4);
        data.put("message5", message5);
        this.configData = StrSubstitutor.replace(configDataTemplateString, data);
    }

    @RequestMapping(value = "", produces = "application/javascript", method = RequestMethod.GET)
    public String getConfigData() {
        return configData;
    }

}
