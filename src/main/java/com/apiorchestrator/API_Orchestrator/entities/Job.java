package com.apiorchestrator.API_Orchestrator.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @Column(name = "group_tag")
    private String groupTag;

    @Column
    private String owner;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "last_run_successful")
    private Boolean lastRunSuccessful;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
}
