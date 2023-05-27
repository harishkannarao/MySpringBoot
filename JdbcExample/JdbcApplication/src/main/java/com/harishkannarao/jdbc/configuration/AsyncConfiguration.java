package com.harishkannarao.jdbc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean("asyncTaskExecutor")
    public Executor taskExecutor(
         @Value("${async.task.executor.pool.size.core}") int corePoolSize,
         @Value("${async.task.executor.pool.size.max}") int maxPoolSize,
         @Value("${async.task.executor.queue.capacity}") int queueCapacity
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsyncTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
