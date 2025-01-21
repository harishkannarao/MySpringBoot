package com.harishkannarao.jdbc.configuration;

import com.harishkannarao.jdbc.entity.type.JsonContentReader;
import com.harishkannarao.jdbc.entity.type.JsonContentWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableJdbcAuditing
@EnableJdbcRepositories(
	basePackages = "com.harishkannarao.jdbc.repository",
	jdbcOperationsRef = "myJdbcOperations",
	transactionManagerRef = "myTransactionManager"
)
public class DataJdbcConfiguration extends AbstractJdbcConfiguration {

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

	@NotNull
	@Override
	protected List<?> userConverters() {
		return Arrays.asList(new JsonContentWriter(), new JsonContentReader());
	}
}
