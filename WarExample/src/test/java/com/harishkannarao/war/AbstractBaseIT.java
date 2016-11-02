package com.harishkannarao.war;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {TestConfigurationWarExampleApplication.class},
        webEnvironment = DEFINED_PORT,
        properties = {
                "server.port=${RANDOM_PORT_5:8185}",
                "spring.config.location=classpath:/spring-boot-default-config/"
        })
public abstract class AbstractBaseIT {}
