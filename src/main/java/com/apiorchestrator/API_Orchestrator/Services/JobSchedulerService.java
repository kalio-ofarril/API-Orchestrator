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

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public JobSchedulerService(JobRepository jobRepository, TaskScheduler taskScheduler,
            JobLogRepository jobLogRepository, WebClient webClient) {
        this.jobRepository = jobRepository;
        this.taskScheduler = taskScheduler;
        this.jobLogRepository = jobLogRepository;
        this.webClient = webClient;
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

    private void runJob(Job job){
        LocalDateTime runTime = LocalDateTime.now();

        try{
            log.info("Running job [{}] at {}", job.getName(), runTime);
            JobLog log = new JobLog();
            log.setJobId(job.getId());
            log.setRunTimestamp(LocalDateTime.now());

            webClient.get()
            .uri(job.getEndpoint())
            .retrieve()
            .toEntity(String.class)
            .doOnSuccess(response -> {
                log.setStatus("SUCCESS");
                log.setResponseCode(response.getStatusCode().value());
                log.setResponseBody(response.getBody());
                job.setLastRunSuccessful(true);
            })
            .doOnError(ex -> {
                log.setStatus("FAILED");
                log.setResponseCode(500);
                log.setErrorMessage(ex.getMessage());
                job.setLastRunSuccessful(false);
            })
            .doFinally(signal -> {
                jobLogRepository.save(log);
                jobRepository.save(job);
            })
            .subscribe();

          
        } catch (Exception ex){
            log.error("Job [{}] failed: {}", job.getName(), ex.getMessage());

            JobLog.builder()
                .jobId(job.getId())
                .runTimestamp(runTime)
                .status("FAILED")
                .errorMessage(ex.getMessage())
                .responseCode(501)
                .build();
        }
    }

}
