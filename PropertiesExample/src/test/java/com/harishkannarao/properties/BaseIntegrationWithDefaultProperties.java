package com.harishkannarao.properties;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.TestSocketUtils;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(
	webEnvironment = DEFINED_PORT,
	classes = {PropertiesApplication.class}
)
@Import({RestAssuredConfiguration.class})
@ActiveProfiles(value = {"default"})
@TestPropertySource(
	properties = {
		"custom.prop2=value3",
	}
)
public abstract class BaseIntegrationWithDefaultProperties {

	@DynamicPropertySource
	static void registerTestProperties(DynamicPropertyRegistry registry) {
		final int RANDOM_SERVER_PORT = TestSocketUtils.findAvailableTcpPort();
		final int RANDOM_MANAGEMENT_PORT = TestSocketUtils.findAvailableTcpPort();
		registry.add("server.port", () -> String.valueOf(RANDOM_SERVER_PORT));
		registry.add("management.port", () -> String.valueOf(RANDOM_MANAGEMENT_PORT));
	}
}
