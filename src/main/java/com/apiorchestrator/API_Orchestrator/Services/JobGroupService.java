package com.apiorchestrator.API_Orchestrator.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.apiorchestrator.API_Orchestrator.Model.Entities.JobGroup;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobGroupRepository;

@Service
public class JobGroupService {

    private final JobGroupRepository jobGroupRepository;

    private static final List<String> AVAILABLE_COLORS = List.of("purple", "cyan", "green", "blue", "orange", "red",
            "teal", "pink");

    JobGroupService(JobGroupRepository jobGroupRepository) {
        this.jobGroupRepository = jobGroupRepository;
    }

    public boolean jobGroupExists(String groupTag) {
        return jobGroupRepository.existsById(groupTag);
    }

    public void manageGroup(String groupTag) {
        if (!jobGroupExists(groupTag)) {
            List<String> usedColors = jobGroupRepository.findAll().stream()
                    .map(JobGroup::getColorTag)
                    .collect(Collectors.toList());

            jobGroupRepository.save(JobGroup
                    .builder()
                    .name(groupTag)
                    .colorTag(pickUnusedColor(usedColors))
                    .build());
        }
    }

    public String pickUnusedColor(List<String> usedColors) {
        return AVAILABLE_COLORS.stream()
                .filter(color -> !usedColors.contains(color))
                .findFirst()
                .orElse("gray"); // fallback if all are used
    }

}
