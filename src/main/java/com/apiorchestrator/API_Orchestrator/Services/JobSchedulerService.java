package com.apiorchestrator.API_Orchestrator.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLog;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobLogRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobSchedulerService {

    private final WebClient webClient;
    private final JobRepository jobRepository;
    private final JobLogRepository jobLogRepository;
    private final TaskScheduler taskScheduler;
    private final JobLockService jobLockService;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public JobSchedulerService(JobRepository jobRepository, TaskScheduler taskScheduler,
            JobLogRepository jobLogRepository, WebClient webClient, JobLockService jobLockService) {
        this.jobRepository = jobRepository;
        this.taskScheduler = taskScheduler;
        this.jobLogRepository = jobLogRepository;
        this.webClient = webClient;
        this.jobLockService = jobLockService;
    }

    @PostConstruct
    public void initialize() {
        log.info("Initiliazing all active jobs:");
        List<Job> activeJobs = jobRepository.findByIsActiveTrue();

        for (Job job : activeJobs) {
            log.info(job.getName());
            scheduleJob(job);
        }
    }

    public void scheduleJob(Job job) {

        if (scheduledTasks.containsKey(job.getId())) {
            unscheduleJob(job.getId());
        }

        CronTrigger cronTrigger = new CronTrigger(job.getCronExpression());
        ScheduledFuture<?> future = taskScheduler.schedule(() -> runJob(job), cronTrigger);

        scheduledTasks.put(job.getId(), future);
    }

    public void unscheduleJob(Long jobId) {
        ScheduledFuture<?> future = scheduledTasks.remove(jobId);
        if (future != null) {
            future.cancel(false);
        }
    }

    public void triggerJob(Job job){
        runJob(job);
    }

    private void runJob(Job job) {
        LocalDateTime runTime = LocalDateTime.now();

        if (!jobLockService.acquireLock(job.getId(), 60)) {
            log.warn("Job [{}] skipped: lock not acquired", job.getName());
            return;
        }

        JobLog logEntry = new JobLog();
        logEntry.setJobId(job.getId());
        logEntry.setRunTimestamp(runTime);

        webClient.get()
                .uri(job.getEndpoint())
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(response -> {
                    log.info("Job [{}] succeeded", job.getName());
                    logEntry.setStatus("SUCCESS");
                    logEntry.setResponseCode(response.getStatusCode().value());
                    logEntry.setResponseBody(response.getBody());
                    job.setLastRunSuccessful(true);
                })
                .doOnError(error -> {
                    log.error("Job [{}] failed: {}", job.getName(), error.getMessage());
                    logEntry.setStatus("FAILED");
                    logEntry.setResponseCode(500);
                    logEntry.setErrorMessage(error.getMessage());
                    job.setLastRunSuccessful(false);
                })
                .doFinally(signal -> {
                    jobLogRepository.save(logEntry);
                    jobRepository.save(job);
                    jobLockService.releaseLock(job.getId());
                })
                .subscribe();
    }

}
