package com.vsms.advisor.service.state;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import org.springframework.stereotype.Component;

@Component
public class CreatedState implements JobCardState {

    @Override
    public void startJob(JobCard jobCard) {
        jobCard.setStatus(JobCardStatus.IN_PROGRESS);
    }

    @Override
    public void requestQa(JobCard jobCard) {
        throw new IllegalStateException("QA can only be requested from IN_PROGRESS state");
    }

    @Override
    public void markReadyForBilling(JobCard jobCard) {
        throw new IllegalStateException("READY_FOR_BILLING can only be set from QA_PENDING state");
    }
}
