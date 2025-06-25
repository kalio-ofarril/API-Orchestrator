package com.apiorchestrator.API_Orchestrator.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "job_groups")
public class JobGroup {

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name; // e.g., "dummysite jobs"

    @Column(name = "color_tag", nullable = false)
    private String colorTag; // e.g., "purple", "cyan"

}
