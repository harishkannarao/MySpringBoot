package com.harishkannarao.jdbc_fixtures;

import com.harishkannarao.jdbc.fixtures.DbFixturesPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication(
        scanBasePackageClasses = {
                DbFixturesPopulator.class
        }
)
public class JdbcApplicationDbFixtures implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(JdbcApplicationDbFixtures.class, args);
    }

    @Autowired
    private DbFixturesPopulator dbFixturesPopulator;
    @Autowired
    private Environment environment;

    @Override
    public void run(String... strings) {
        if (environment.getProperty("application.run", Boolean.class, Boolean.TRUE)) {
            dbFixturesPopulator.createSchema();
            dbFixturesPopulator.insertData();
        }
    }
}
