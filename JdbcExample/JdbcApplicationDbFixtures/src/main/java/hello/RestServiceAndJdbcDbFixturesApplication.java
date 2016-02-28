package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestServiceAndJdbcDbFixturesApplication implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(RestServiceAndJdbcDbFixturesApplication.class, args);
    }

    @Autowired
    private DbFixturesPopulator dbFixturesPopulator;

    @Override
    public void run(String... strings) throws Exception {
        dbFixturesPopulator.createSchema();
        dbFixturesPopulator.insertData();
    }
}
