package com.apiorchestrator.API_Orchestrator.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class SchedulerConfig {
    
    @Value("${scheduler.pool.size}")
    private Integer poolSize;

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("api-orchestrator-job-scheduler-");
        scheduler.initialize();
        return scheduler;
    }

}
