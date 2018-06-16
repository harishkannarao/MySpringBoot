package com.harishkannarao.jdbc_fixtures;

import org.junit.After;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public abstract class BaseIntegrationTestWithRestServiceAndJdbcDbFixturesApplication {

    static ConfigurableApplicationContext application;
    private static String[] defaultProperties = new String[]{"--application.run=false"};

    static void runSpringApplication() {
        application =  SpringApplication.run(
                JdbcApplicationDbFixtures.class,
                defaultProperties
        );
    }

    @After
    public void stopSpringApplication() {
        if (application != null && application.isRunning()) {
            SpringApplication.exit(application);
        }
    }
}
