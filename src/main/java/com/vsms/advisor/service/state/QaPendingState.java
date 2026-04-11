package com.vsms.advisor.service.state;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import org.springframework.stereotype.Component;

@Component
public class QaPendingState implements JobCardState {

    @Override
    public void startJob(JobCard jobCard) {
        throw new IllegalStateException("Job cannot return to IN_PROGRESS from QA_PENDING");
    }

    @Override
    public void requestQa(JobCard jobCard) {
        throw new IllegalStateException("QA already pending");
    }

    @Override
    public void markReadyForBilling(JobCard jobCard) {
        jobCard.setStatus(JobCardStatus.READY_FOR_BILLING);
    }
}
