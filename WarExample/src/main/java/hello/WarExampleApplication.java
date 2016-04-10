package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class WarExampleApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder
				.sources(WarExampleApplication.class)
				.properties(getPropertiesForWar());
	}

	private static Properties getPropertiesForWar() {
		List<String> configLocations = new ArrayList<>();
		configLocations.add("classpath:/war-default-config/");
		String warExampleConfigLocation = System.getenv("WAR_EXAMPLE_CONFIG_LOCATION");
		if(warExampleConfigLocation != null){
			String formattedWarExampleConfigLocation = String.format("file:%s/", warExampleConfigLocation);
			configLocations.add(formattedWarExampleConfigLocation);
		}
		String spingConfigValues = StringUtils.collectionToCommaDelimitedString(configLocations);

		Properties props = new Properties();
		props.put("spring.config.location", spingConfigValues);
		return props;
	}

	public static void main(String[] args) {
		List<String> arguments = new ArrayList<>(Arrays.asList(args));
		boolean containsSpringConfigParam = arguments.stream().anyMatch(s -> s.contains("spring.config.location"));
		if(!containsSpringConfigParam) {
			arguments.add(getSpringConfigParam());
		}

		String[] modifiedArgs = new String[arguments.size()];
		for (int i = 0; i < arguments.size(); i++) {
			modifiedArgs[i] = arguments.get(i);
		}

		SpringApplication.run(WarExampleApplication.class, modifiedArgs);
	}

	private static String getSpringConfigParam() {
		List<String> configLocations = new ArrayList<>();
		configLocations.add("classpath:/spring-boot-default-config/");
		String warExampleConfigLocation = System.getenv("WAR_EXAMPLE_CONFIG_LOCATION");
		if(warExampleConfigLocation != null){
			String formattedWarExampleConfigLocation = String.format("file:%s/", warExampleConfigLocation);
			configLocations.add(formattedWarExampleConfigLocation);
		}
		String spingConfigValues = StringUtils.collectionToCommaDelimitedString(configLocations);
		return String.format("--spring.config.location=%s", spingConfigValues);
	}

}
