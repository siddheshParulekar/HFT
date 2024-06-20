package com.thrift.hft.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    private static final Logger logger = LogManager.getLogger(AsyncConfiguration.class);

    @Bean
    public TaskDecorator securityContextTaskDecorator() {
        return new TaskDecorator() {
            public Runnable decorate(Runnable runnable) {
                SecurityContext context = SecurityContextHolder.getContext();
                return () -> {
                    SecurityContextHolder.setContext(context);
                    try {
                        runnable.run();
                    } finally {
                        SecurityContextHolder.clearContext();
                    }
                };
            }
        };
    }

    @Bean(name ="asyncTaskExecutor")
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("asyncTaskThread-");
        executor.setTaskDecorator(securityContextTaskDecorator());
        executor.initialize();
        logger.info("Using asyncTaskExecutor "+ executor.getClass().getSimpleName());
        return executor;
    }
}
