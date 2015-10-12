package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceAndJdbcDbFixturesApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(RestServiceAndJdbcDbFixturesApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(RestServiceAndJdbcDbFixturesApplication.class, args);
    }

    @Autowired
    private DbFixturesPopulator dbFixturesPopulator;

    @Override
    public void run(String... strings) throws Exception {
        log.info("Creating schema");
        dbFixturesPopulator.createSchema();
        log.info("Inserting data");
        dbFixturesPopulator.insertData();
        log.info("Db Fixtures setup");
    }
}
