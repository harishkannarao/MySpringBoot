package com.harishkannarao.jdbc;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public final class Postgres {

	private final int port = 5432;
	private final String userEnvVar = "POSTGRES_USER";
	private final String passEnvVar = "POSTGRES_PASSWORD";

	private final GenericContainer<?> container = new GenericContainer<>(
		DockerImageName.parse("public.ecr.aws/docker/library/postgres:16"))
		.withReuse(true)
		.withExposedPorts(port)
		.withEnv(userEnvVar, "myuser")
		.withEnv(passEnvVar, "superpassword")
		.waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1));

	public GenericContainer<?> getContainer() {
		return container;
	}

	public Integer getMappedPort() {
		return container.getMappedPort(port);
	}

	public String getUsername() {
		return container.getEnvMap().get(userEnvVar);
	}

	public String getPassword() {
		return container.getEnvMap().get(passEnvVar);
	}
}
