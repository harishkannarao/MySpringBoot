package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class WarExampleApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WarExampleApplication.class, args);
	}

}
