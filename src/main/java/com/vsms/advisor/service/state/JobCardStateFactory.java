package com.vsms.advisor.service.state;

import com.vsms.advisor.entity.JobCardStatus;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JobCardStateFactory {

    private final Map<JobCardStatus, JobCardState> stateMap;

    public JobCardStateFactory(
        CreatedState createdState,
        InProgressState inProgressState,
        QaPendingState qaPendingState,
        ReadyForBillingState readyForBillingState
    ) {
        this.stateMap = new EnumMap<>(JobCardStatus.class);
        this.stateMap.put(JobCardStatus.CREATED, createdState);
        this.stateMap.put(JobCardStatus.IN_PROGRESS, inProgressState);
        this.stateMap.put(JobCardStatus.QA_PENDING, qaPendingState);
        this.stateMap.put(JobCardStatus.READY_FOR_BILLING, readyForBillingState);
    }

    public JobCardState getState(JobCardStatus status) {
        JobCardState state = stateMap.get(status);
        if (state == null) {
            throw new IllegalArgumentException("Unsupported JobCardStatus: " + status);
        }
        return state;
    }
}
