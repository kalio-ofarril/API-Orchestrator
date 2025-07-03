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
@Table(
    name = "job_logs",
    indexes = {
        @Index(name = "idx_job_id", columnList = "job_id"),
        @Index(name = "idx_status", columnList = "status")
    }
)
public class JobLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each log must be associated with a Job
    @JoinColumn(name = "job_id", nullable = false)
    private Long jobId;

    @Builder.Default
    @Column(name = "run_timestamp", nullable = false)
    private LocalDateTime runTimestamp = LocalDateTime.now();

    /**
     * Execution status for the job:
     * SUCCESS — completed successfully
     * FAILED — execution failed
     * TIMEOUT — did not complete in time
     * SKIPPED — skipped by logic or policy
     */
    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Column(name = "response_code")
    private Integer responseCode;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

}
