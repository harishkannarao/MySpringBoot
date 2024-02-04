package com.harishkannarao.jdbc.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

	@Bean
	@ConfigurationProperties("app.datasource")
	@Qualifier("myDataSource")
	@FlywayDataSource
	public DataSource creatDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	@Qualifier("myJdbcTemplate")
	public JdbcTemplate createJdbcTemplate(@Qualifier("myDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}


	@Bean
	@Qualifier("myNamedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate createNamedParameterJdbcTemplate(
		@Qualifier("myDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
