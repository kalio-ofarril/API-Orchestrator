package com.apiorchestrator.API_Orchestrator.Model.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "jobs", indexes = {
        @Index(name = "idx_group_tag", columnList = "group_tag"),
        @Index(name = "idx_owner", columnList = "owner")
})
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Lob
    @Column(nullable = true)
    private String description;

    @Column(name = "endpoint", nullable = false, length = 255)
    private String endpoint;

    @Column(name = "cron_expression", nullable = false, length = 100)
    private String cronExpression;

    @Column(name = "group_tag", length = 100)
    private String groupTag;

    @Column(length = 100)
    private String owner;

    @Builder.Default
    @Column(name = "is_active", nullable = false, columnDefinition = "SMALLINT DEFAULT 1")
    private boolean isActive = true;

    @Builder.Default
    @Column(name = "last_run_successful", nullable = false)
    private Boolean lastRunSuccessful = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
