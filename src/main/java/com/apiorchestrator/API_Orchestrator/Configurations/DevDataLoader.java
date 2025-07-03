package com.apiorchestrator.API_Orchestrator.Configurations;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLog;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobGroupRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobLogRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobRepository;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobGroup;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DevDataLoader {

    private final JobRepository jobRepository;
    private final JobGroupRepository jobGroupRepository;
    private final JobLogRepository jobLogRepository;

    public DevDataLoader(JobRepository jobRepository,
                         JobGroupRepository jobGroupRepository,
                         JobLogRepository jobLogRepository) {
        this.jobRepository = jobRepository;
        this.jobGroupRepository = jobGroupRepository;
        this.jobLogRepository = jobLogRepository;
    }

    @PostConstruct
    public void loadData() {
        // Create groups
        JobGroup group1 = JobGroup.builder().name("dummysite jobs").colorTag("purple").build();
        JobGroup group2 = JobGroup.builder().name("fakesite jobs").colorTag("cyan").build();

        jobGroupRepository.saveAll(List.of(group1, group2));

        // Create jobs with mock endpoints
        Job job1 = Job.builder()
                .name("Mock Success Job")
                .description("Returns 200 OK every 5 seconds")
                .endpoint("http://httpbin.org/status/200")
                .cronExpression("*/10 * * * * *")  // every 5 seconds
                .groupTag("dummysite jobs")
                .owner("kalio")
                .isActive(true)
                .lastRunSuccessful(true)
                .build();

        Job job2 = Job.builder()
                .name("Mock Failure Job")
                .description("Returns 500 Error every 10 seconds")
                .endpoint("http://httpbin.org/status/500")
                .cronExpression("*/30 * * * * *") // every 10 seconds
                .groupTag("fakesite jobs")
                .owner("kalio")
                .isActive(true)
                .lastRunSuccessful(false)
                .build();

        jobRepository.saveAll(List.of(job1, job2));

        // Add logs
        jobLogRepository.saveAll(List.of(
                JobLog.builder()
                        .jobId(job1.getId())
                        .runTimestamp(LocalDateTime.now().minusHours(5))
                        .status("SUCCESS")
                        .responseCode(200)
                        .responseBody("OK")
                        .build(),
                JobLog.builder()
                        .jobId(job1.getId())
                        .runTimestamp(LocalDateTime.now().minusDays(1))
                        .status("FAILED")
                        .responseCode(500)
                        .errorMessage("Server error")
                        .build(),
                JobLog.builder()
                        .jobId(job2.getId())
                        .runTimestamp(LocalDateTime.now().minusHours(2))
                        .status("FAILED")
                        .responseCode(404)
                        .errorMessage("Endpoint not found")
                        .build()
        ));
    }
}
