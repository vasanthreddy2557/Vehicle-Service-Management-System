package com.vsms.mechanic.service.observer;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import com.vsms.advisor.repository.JobCardRepository;
import com.vsms.mechanic.entity.ServiceTaskStatus;
import com.vsms.mechanic.repository.ServiceTaskRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JobCardTaskCompletionObserver implements TaskCompletionObserver {

    private final ServiceTaskRepository serviceTaskRepository;
    private final JobCardRepository jobCardRepository;

    public JobCardTaskCompletionObserver(
        ServiceTaskRepository serviceTaskRepository,
        JobCardRepository jobCardRepository
    ) {
        this.serviceTaskRepository = serviceTaskRepository;
        this.jobCardRepository = jobCardRepository;
    }

    @Override
    @Transactional
    public void onTaskCompleted(Long jobCardId) {
        boolean hasIncompleteTasks = serviceTaskRepository.existsByJobCardIdAndStatusNot(
            jobCardId,
            ServiceTaskStatus.COMPLETED
        );

        if (!hasIncompleteTasks) {
            JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new IllegalArgumentException("JobCard not found: " + jobCardId));

            if (jobCard.getStatus() == JobCardStatus.IN_PROGRESS) {
                jobCard.setStatus(JobCardStatus.QA_PENDING);
                jobCardRepository.save(jobCard);
            }
        }
    }
}
