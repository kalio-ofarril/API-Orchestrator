package com.apiorchestrator.API_Orchestrator.Services;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLog;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobLogRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final JobLogRepository jobLogRepository;
    private final JobSchedulerService jobSchedulerService;
    private final JobGroupService jobGroupService;

    public JobService(JobRepository jobRepository, JobLogRepository jobLogRepository, JobSchedulerService jobSchedulerService, JobGroupService jobGroupService){
        this.jobRepository = jobRepository;
        this.jobLogRepository = jobLogRepository;
        this.jobSchedulerService = jobSchedulerService;
        this.jobGroupService = jobGroupService;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
    }

    public Job createJob(Job job) {
        jobRepository.save(job);
        scheduleJob(job);
        jobGroupService.manageGroup(job.getGroupTag());
        return job;
    }

    public Job updateJob(Long id, Job jobDetails) {
        Job existingJob = getJobById(id);

        existingJob.setName(jobDetails.getName());
        existingJob.setDescription(jobDetails.getDescription());
        existingJob.setEndpoint(jobDetails.getEndpoint());
        existingJob.setCronExpression(jobDetails.getCronExpression());
        existingJob.setGroupTag(jobDetails.getGroupTag());
        existingJob.setOwner(jobDetails.getOwner());
        existingJob.setActive(jobDetails.isActive());
        existingJob.setLastRunSuccessful(jobDetails.getLastRunSuccessful());

        jobRepository.save(existingJob);

        scheduleJob(existingJob);
        return existingJob; 
    }

    public void deleteJob(Long id) {
        jobSchedulerService.unscheduleJob(id); 
        jobRepository.deleteById(id);
    }

    public void triggerJob(Long id) {
        Job job = getJobById(id);
        log.info("Manually triggering job [{}] via API", job.getName());
        jobSchedulerService.triggerJob(job);
    }

    public void scheduleJob(Job job){
        if(job.isActive()){
            jobSchedulerService.scheduleJob(job);
        }else{
            jobSchedulerService.unscheduleJob(job.getId());
        }
    }

    public List<JobLog> getJobLogs(Long jobId) {
        return jobLogRepository.findByJobId(jobId);
    }
}
