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
@Table(name = "job_locks")
public class JobLock {

    @Id
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "locked_until", nullable = false)
    private LocalDateTime lockedUntil;
}
