package com.harishkannarao.jdbc.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.flyway.autoconfigure.FlywayDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

	@Bean
	@ConfigurationProperties("app.datasource.hikari")
	@Qualifier("myDataSource")
	@FlywayDataSource
	public HikariDataSource createDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	@Qualifier("myJdbcClient")
	public JdbcClient createJdbcClient(@Qualifier("myDataSource") DataSource dataSource) {
		return JdbcClient.create(dataSource);
	}
}
