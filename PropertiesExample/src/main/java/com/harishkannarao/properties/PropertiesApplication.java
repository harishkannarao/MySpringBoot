package com.harishkannarao.properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PropertiesApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertiesApplication.class, args);
    }
}
