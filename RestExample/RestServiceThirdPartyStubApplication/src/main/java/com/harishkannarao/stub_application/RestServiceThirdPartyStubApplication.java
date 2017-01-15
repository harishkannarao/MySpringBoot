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

    public static void main(String[] args) {
        SpringApplication.run(RestServiceThirdPartyStubApplication.class, args);
    }
}
