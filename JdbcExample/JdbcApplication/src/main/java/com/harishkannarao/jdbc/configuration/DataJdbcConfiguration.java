package com.harishkannarao.jdbc.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
// This class in needed only when we have multiple data sources or databases
// It allows to inject different data sources for CRUD repository in different packages
@EnableJdbcRepositories(
	basePackages = "com.harishkannarao.jdbc.repository",
	jdbcOperationsRef = "myJdbcOperations",
	transactionManagerRef = "myTransactionManager"
)
public class DataJdbcConfiguration {

	@Bean(name = "myJdbcOperations")
	public NamedParameterJdbcOperations createNamedParameterJdbcOperations(
		@Qualifier("myDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean(name = "myTransactionManager")
	public PlatformTransactionManager createPlatformTransactionManager(
		@Qualifier("myDataSource") DataSource dataSource) {
		return new JdbcTransactionManager(dataSource);
	}
}
