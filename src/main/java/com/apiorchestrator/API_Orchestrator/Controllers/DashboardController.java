package com.apiorchestrator.API_Orchestrator.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apiorchestrator.API_Orchestrator.Model.DTOs.AllDashboardDataDTO;
import com.apiorchestrator.API_Orchestrator.Services.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public AllDashboardDataDTO getDashboardData() {
        return dashboardService.getDashboardData();
    }
}

