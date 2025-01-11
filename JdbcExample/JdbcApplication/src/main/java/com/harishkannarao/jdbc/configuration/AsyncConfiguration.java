package com.harishkannarao.jdbc.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfiguration {
	private static final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

	@Bean("asyncTaskExecutor")
	public Executor asyncTaskExecutor(
		@Value("${async.task.executor.pool.size.core}") int corePoolSize,
		@Value("${async.task.executor.pool.size.max}") int maxPoolSize,
		@Value("${async.task.executor.queue.capacity}") int queueCapacity
	) {
		log.info("corePoolSize: {} maxPoolSize: {} queueCapacity: {}",
			corePoolSize, maxPoolSize, queueCapacity);
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("AsyncTaskExecutor-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}
}
