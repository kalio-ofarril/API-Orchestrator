package com.apiorchestrator.API_Orchestrator.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLock;

public interface JobLockRepository extends JpaRepository<JobLock, Long> {
    
}
