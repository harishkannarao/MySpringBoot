package com.harishkannarao.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.web.client.RestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
	webEnvironment = DEFINED_PORT,
	classes = {PropertiesApplication.class}
)
@Import({TestConfigurationPropertiesApplication.class})
@ActiveProfiles(value = {"default"})
@TestPropertySource(
	properties = {
		"properties.endpoint.url=http://localhost:${server.port}",
	}
)
public abstract class BaseIntegrationWithDefaultProperties {
	@Autowired
	@Qualifier("myTestRestTemplate")
	protected RestTemplate testRestTemplate;

	@DynamicPropertySource
	static void registerTestProperties(DynamicPropertyRegistry registry) {
		final int RANDOM_SERVER_PORT = TestSocketUtils.findAvailableTcpPort();
		final int RANDOM_MANAGEMENT_PORT = TestSocketUtils.findAvailableTcpPort();
		registry.add("server.port", () -> String.valueOf(RANDOM_SERVER_PORT));
		registry.add("management.port", () -> String.valueOf(RANDOM_MANAGEMENT_PORT));
	}
}
