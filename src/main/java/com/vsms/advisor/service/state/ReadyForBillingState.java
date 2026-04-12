package com.vsms.advisor.service.state;

import com.vsms.advisor.entity.JobCard;
import org.springframework.stereotype.Component;

@Component
public class ReadyForBillingState implements JobCardState {

    @Override
    public void startJob(JobCard jobCard) {
        throw new IllegalStateException("Job is already READY_FOR_BILLING");
    }

    @Override
    public void requestQa(JobCard jobCard) {
        throw new IllegalStateException("Job is already READY_FOR_BILLING");
    }

    @Override
    public void markReadyForBilling(JobCard jobCard) {
        throw new IllegalStateException("Job is already READY_FOR_BILLING");
    }
}
