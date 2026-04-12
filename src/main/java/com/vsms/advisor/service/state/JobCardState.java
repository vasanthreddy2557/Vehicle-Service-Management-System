package com.vsms.advisor.service.state;

import com.vsms.advisor.entity.JobCard;

public interface JobCardState {

    void startJob(JobCard jobCard);

    void requestQa(JobCard jobCard);

    void markReadyForBilling(JobCard jobCard);
}
