package com.vsms.advisor.service.state;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import org.springframework.stereotype.Component;

@Component
public class InProgressState implements JobCardState {

    @Override
    public void startJob(JobCard jobCard) {
        throw new IllegalStateException("Job is already IN_PROGRESS");
    }

    @Override
    public void requestQa(JobCard jobCard) {
        jobCard.setStatus(JobCardStatus.QA_PENDING);
    }

    @Override
    public void markReadyForBilling(JobCard jobCard) {
        throw new IllegalStateException("READY_FOR_BILLING can only be set from QA_PENDING state");
    }
}
