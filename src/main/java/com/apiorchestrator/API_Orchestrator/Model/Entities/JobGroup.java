package com.apiorchestrator.API_Orchestrator.Model.Entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "job_groups",
    indexes = {
        @Index(name = "idx_group_name", columnList = "name")
    }
)
public class JobGroup {

    @Id
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Color name or token to use for group visual identification.
     * E.g., "purple", "cyan", "gray"
     */
    @Column(name = "color_tag", nullable = false, length = 50)
    private String colorTag;
}
