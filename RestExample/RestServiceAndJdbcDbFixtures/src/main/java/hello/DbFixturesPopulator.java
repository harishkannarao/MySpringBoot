package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbFixturesPopulator {
    private static final Logger log = LoggerFactory.getLogger(DbFixturesPopulator.class);

    private static boolean SCHEMA_INITIALIZED = false;

    private DataSource dataSource;

    @Autowired
    public DbFixturesPopulator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createSchema() {
        log.info("Creating schema in database");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("/dbscripts/create-test-schema.sql"));
        populator.execute(this.dataSource);
    }

    public void initSchema() {
        if (!SCHEMA_INITIALIZED) {
            log.info("Initializing schema in database");
            createSchema();
            SCHEMA_INITIALIZED=true;
        } else {
            log.info("Database schema is already initialized");
        }
    }

    public void insertData() {
        log.info("Inserting data in database");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("/dbscripts/create-test-data.sql"));
        populator.execute(this.dataSource);
    }

    public void resetData() {
        log.info("Resetting data in database");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("/dbscripts/delete-test-data.sql"),
                new ClassPathResource("/dbscripts/create-test-data.sql"));
        populator.execute(this.dataSource);
    }

}
