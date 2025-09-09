/**
 * 
 */
package com.hitech.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author dinesh.jakhar
 *
 */
//@EnableAsync
//@Configuration
public class AsyncConfig {

//	@Bean(name = "asyncExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5); // set the core pool size
		executor.setMaxPoolSize(10); // max pool size
		executor.setQueueCapacity(300);
		executor.setThreadNamePrefix("ext-async-"); // give an optional name to your threads
		executor.initialize();
		return executor;
	}
}
