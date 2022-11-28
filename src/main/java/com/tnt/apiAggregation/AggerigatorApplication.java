package com.tnt.apiAggregation;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tnt.apiAggregation.utility.InternalService;


@SpringBootApplication
@EnableScheduling
public class AggerigatorApplication {
	@Autowired
	InternalService internalService;

	public static void main(String[] args) {
		SpringApplication.run(AggerigatorApplication.class, args);
	}
	
	@Scheduled(initialDelay = 600000, fixedDelay = 600000)
	public void scheduledCleaning()
	{
		internalService.cleanResponses();
	}
	
	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(200);
		executor.setCorePoolSize(200);
		executor.setQueueCapacity(600);
		executor.setThreadNamePrefix("concurrThread-");
		executor.initialize();
		executor.setKeepAliveSeconds(10);
		return executor;
	}
	
}
