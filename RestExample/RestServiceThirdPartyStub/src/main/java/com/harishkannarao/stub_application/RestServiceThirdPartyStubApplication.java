package com.harishkannarao.stub_application;

import com.harishkannarao.rest.stub.SupportConfigurationClassForAutoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(
        scanBasePackageClasses = {
                SupportConfigurationClassForAutoScan.class
        }
)
public class RestServiceThirdPartyStubApplication {
    private static ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {
        configurableApplicationContext = SpringApplication.run(RestServiceThirdPartyStubApplication.class, args);
    }

    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return configurableApplicationContext;
    }
}
