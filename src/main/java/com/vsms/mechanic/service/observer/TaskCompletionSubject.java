package com.vsms.mechanic.service.observer;

public interface TaskCompletionSubject {

    void registerObserver(TaskCompletionObserver observer);

    void unregisterObserver(TaskCompletionObserver observer);

    void notifyTaskCompleted(Long jobCardId);
}
