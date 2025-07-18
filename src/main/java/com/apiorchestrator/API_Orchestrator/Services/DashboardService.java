package com.apiorchestrator.API_Orchestrator.Services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.apiorchestrator.API_Orchestrator.Model.DTOs.AllDashboardDataDTO;
import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;
import com.apiorchestrator.API_Orchestrator.Model.Entities.JobGroup;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobGroupRepository;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobRepository;

@Service
public class DashboardService {

    private final JobGroupRepository jobGroupRepository;
    private final JobRepository jobRepository;

    public DashboardService(JobGroupRepository jobGroupRepository, JobRepository jobRepository) {
        this.jobGroupRepository = jobGroupRepository;
        this.jobRepository = jobRepository;
    }

    public AllDashboardDataDTO getDashboardData() {

        List<Job> jobs = jobRepository.findAll();

        Map<String, String> groupColorMap = jobGroupRepository.findAll()
                .stream()
                .collect(Collectors.toMap(JobGroup::getName, JobGroup::getColorTag));

        return AllDashboardDataDTO
                .builder()
                .jobs(jobs)
                .groupColorMap(groupColorMap)
                .build();
    }

}
