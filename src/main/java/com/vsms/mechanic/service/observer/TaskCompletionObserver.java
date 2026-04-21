package com.vsms.mechanic.service.observer;

public interface TaskCompletionObserver {

    void onTaskCompleted(Long jobCardId);
}
