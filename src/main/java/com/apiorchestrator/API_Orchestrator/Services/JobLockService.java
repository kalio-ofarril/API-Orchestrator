package com.apiorchestrator.API_Orchestrator.Services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apiorchestrator.API_Orchestrator.Model.Entities.JobLock;
import com.apiorchestrator.API_Orchestrator.Model.Repositories.JobLockRepository;

import jakarta.transaction.Transactional;

@Service
public class JobLockService {

    private final JobLockRepository jobLockRepository;

    public JobLockService(JobLockRepository jobLockRepository) {
        this.jobLockRepository = jobLockRepository;
    }

    @Transactional
    public boolean acquireLock(Long jobId, int lockDurationSeconds) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(lockDurationSeconds);

        Optional<JobLock> existingLock = jobLockRepository.findById(jobId);

        if (existingLock.isEmpty()) {
            // No lock, create a new one
            jobLockRepository.save(new JobLock(jobId, expiresAt));
            return true;
        }

        JobLock lock = existingLock.get();
        if (lock.getLockedUntil().isBefore(now)) {
            // Lock is expired, create new lock
            lock.setLockedUntil(expiresAt);
            jobLockRepository.save(lock);
        }

        // Lock is valid
        return false;
    }

    @Transactional
    public void releaseLock(Long logId) {
        jobLockRepository.deleteById(logId);
    }

}
