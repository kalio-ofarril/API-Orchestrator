package com.apiorchestrator.API_Orchestrator.Model.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;

public interface JobRepository extends JpaRepository<Job, Long>{
    
}
