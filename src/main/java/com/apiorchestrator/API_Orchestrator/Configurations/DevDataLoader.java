package com.apiorchestrator.API_Orchestrator.Configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobGroup;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobGroupRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobRepository;

@Component
@Profile("dev") // This ensures it only runs in the H2 dev profile
public class DevDataLoader implements CommandLineRunner {

    private final JobRepository jobRepository;
    private final JobGroupRepository jobGroupRepository;

    public DevDataLoader(JobRepository jobRepository, JobGroupRepository jobGroupRepository) {
        this.jobRepository = jobRepository;
        this.jobGroupRepository = jobGroupRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create Job Groups
        jobGroupRepository.save(
                JobGroup.builder()
                        .name("dummysite jobs")
                        .colorTag("purple")
                        .build());

        jobGroupRepository.save(
                JobGroup.builder()
                        .name("fakesite jobs")
                        .colorTag("cyan")
                        .build());

        // Create Jobs
        jobRepository.save(jobRepository.save(
                Job.builder()
                        .name("First Job")
                        .description("Short description of the job.")
                        .endpoint("https://dummysite/url")
                        .cronExpression("0 0/30 9-17 * * ?")
                        .groupTag("dummysite jobs")
                        .owner("Kalio")
                        .isActive(true)
                        .lastRunSuccessful(true)
                        .build()));

        jobRepository.save(jobRepository.save(
                Job.builder()
                        .name("Second Job")
                        .description("Another scheduled job.")
                        .endpoint("https://fakesite/url")
                        .cronExpression("0 0 12 * * ?")
                        .groupTag("fakesite jobs")
                        .owner("Kalio")
                        .isActive(true)
                        .lastRunSuccessful(false)
                        .build()));
    }
}
