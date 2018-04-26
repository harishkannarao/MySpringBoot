package com.harishkannarao.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.StringUtils;

import java.util.*;

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
			StringBuilder configLocationBuilder = new StringBuilder("file:");
			configLocationBuilder.append(warExampleConfigLocation);
			if(!warExampleConfigLocation.endsWith("/")) {
				configLocationBuilder.append("/");
			}
			configLocations.add(configLocationBuilder.toString());
		}
		String springConfigValues = StringUtils.collectionToCommaDelimitedString(configLocations);
		String warExampleActiveProfiles = System.getenv("WAR_EXAMPLE_ACTIVE_PROFILES");

		Properties props = new Properties();
		if(warExampleActiveProfiles != null) {
			props.put("spring.profiles.active", warExampleActiveProfiles);
		}
		props.put("spring.config.location", springConfigValues);

		return props;
	}

	public static void main(String[] args) {
		List<String> arguments = new ArrayList<>(Arrays.asList(args));
		boolean containsSpringConfigParam = arguments.stream().anyMatch(s -> s.contains("spring.config.location"));
		if(!containsSpringConfigParam) {
			arguments.add(getSpringConfigParam());
		}
		boolean containsActiveProfilesParam = arguments.stream().anyMatch(s -> s.contains("spring.profiles.active"));
		if(!containsActiveProfilesParam) {
			Optional<String> springActiveProfilesParam = getSpringActiveProfilesParam();
			if(springActiveProfilesParam.isPresent()) {
				arguments.add(springActiveProfilesParam.get());
			}
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
			StringBuilder configLocationBuilder = new StringBuilder("file:");
			configLocationBuilder.append(warExampleConfigLocation);
			if(!warExampleConfigLocation.endsWith("/")) {
				configLocationBuilder.append("/");
			}
			configLocations.add(configLocationBuilder.toString());
		}
		String springConfigValues = StringUtils.collectionToCommaDelimitedString(configLocations);
		return String.format("--spring.config.location=%s", springConfigValues);
	}

	private static Optional<String> getSpringActiveProfilesParam() {
		Optional<String> result = Optional.empty();
		String warExampleActiveProfiles = System.getenv("WAR_EXAMPLE_ACTIVE_PROFILES");
		if(warExampleActiveProfiles != null) {
			result = Optional.of(String.format("--spring.profiles.active=%s", warExampleActiveProfiles));
		}
		return result;
	}

}
