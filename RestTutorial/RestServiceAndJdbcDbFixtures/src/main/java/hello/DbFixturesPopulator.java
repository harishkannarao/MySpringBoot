package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbFixturesPopulator {

    private DataSource dataSource;

    @Autowired
    public DbFixturesPopulator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createSchema() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("/dbscripts/create-test-schema.sql"));
        populator.execute(this.dataSource);
    }

    public void insertData() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("/dbscripts/create-test-data.sql"));
        populator.execute(this.dataSource);
    }

    public void resetData() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("/dbscripts/delete-test-data.sql"),
                new ClassPathResource("/dbscripts/create-test-data.sql"));
        populator.execute(this.dataSource);
    }

}
