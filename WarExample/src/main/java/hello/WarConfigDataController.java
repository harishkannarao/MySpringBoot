package hello;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@RequestMapping(value = "configdata.js")
public class WarConfigDataController {

    private final Resource configDataTemplate = new ClassPathResource("/template/config_data.js");

    private final String configData;

    public WarConfigDataController() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(configDataTemplate.getURI()));
        this.configData = new String(bytes);
    }

    @RequestMapping(value = "", produces = "application/javascript", method = RequestMethod.GET)
    public String getConfigData() {
        return configData;
    }

}
