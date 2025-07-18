package com.apiorchestrator.API_Orchestrator.Model.DTOs;

import java.util.List;
import java.util.Map;

import com.apiorchestrator.API_Orchestrator.Model.Entities.Job;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AllDashboardDataDTO {

    public List<Job> jobs;

    Map<String, String> groupColorMap;
    
}
