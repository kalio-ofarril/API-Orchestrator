package com.apiorchestrator.API_Orchestrator.Model.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLog;

public interface JobLogRepository extends JpaRepository<JobLog, Long> {
    List<JobLog> findByJobId(Long jobId);
}
