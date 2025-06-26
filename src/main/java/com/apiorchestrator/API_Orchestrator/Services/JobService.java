package com.apiorchestrator.API_Orchestrator.Services;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLog;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobLogRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLogRepository jobLogRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
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

        return jobRepository.save(existingJob);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public void triggerJob(Long id) {
        Job job = getJobById(id);
        // Logic to trigger the job manually goes here (e.g., make HTTP call, log response, etc.)
    }

    public List<JobLog> getJobLogs(Long jobId) {
        return jobLogRepository.findByJobId(jobId);
    }
}
