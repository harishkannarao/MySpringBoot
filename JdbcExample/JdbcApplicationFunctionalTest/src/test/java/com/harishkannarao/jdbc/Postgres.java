package com.harishkannarao.jdbc;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public final class Postgres {

	public final static GenericContainer<?> CONTAINER = new GenericContainer<>(
		DockerImageName.parse("public.ecr.aws/docker/library/postgres:16"))
		.withReuse(true)
		.withExposedPorts(5432)
		.withEnv("POSTGRES_USER", "myuser")
		.withEnv("POSTGRES_PASSWORD", "superpassword");
}
