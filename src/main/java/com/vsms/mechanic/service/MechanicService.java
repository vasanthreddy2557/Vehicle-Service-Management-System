package com.vsms.mechanic.service;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.repository.JobCardRepository;
import com.vsms.mechanic.entity.Mechanic;
import com.vsms.mechanic.entity.ServiceTask;
import com.vsms.mechanic.entity.ServiceTaskStatus;
import com.vsms.mechanic.repository.MechanicRepository;
import com.vsms.mechanic.repository.ServiceTaskRepository;
import com.vsms.mechanic.service.observer.JobCardTaskCompletionObserver;
import com.vsms.mechanic.service.observer.TaskCompletionObserver;
import com.vsms.mechanic.service.observer.TaskCompletionSubject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MechanicService implements TaskCompletionSubject {

    private final ServiceTaskRepository serviceTaskRepository;
    private final JobCardRepository jobCardRepository;
    private final MechanicRepository mechanicRepository;
    private final List<TaskCompletionObserver> observers = new ArrayList<>();

    public MechanicService(
        ServiceTaskRepository serviceTaskRepository,
        JobCardRepository jobCardRepository,
        MechanicRepository mechanicRepository,
        JobCardTaskCompletionObserver jobCardTaskCompletionObserver
    ) {
        this.serviceTaskRepository = serviceTaskRepository;
        this.jobCardRepository = jobCardRepository;
        this.mechanicRepository = mechanicRepository;
        this.registerObserver(jobCardTaskCompletionObserver);
    }

    @Transactional
    public ServiceTask assignTask(
        Long jobCardId,
        Long mechanicId,
        String taskName,
        String taskDescription,
        BigDecimal laborCost
    ) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
            .orElseThrow(() -> new IllegalArgumentException("JobCard not found: " + jobCardId));

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
            .orElseThrow(() -> new IllegalArgumentException("Mechanic not found: " + mechanicId));

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setJobCard(jobCard);
        serviceTask.setMechanic(mechanic);
        serviceTask.setTaskName(taskName);
        serviceTask.setTaskDescription(taskDescription);
        serviceTask.setLaborCost(laborCost);
        serviceTask.setStatus(ServiceTaskStatus.PENDING);

        return serviceTaskRepository.save(serviceTask);
    }

    @Transactional
    public ServiceTask markTaskInProgress(Long serviceTaskId) {
        ServiceTask serviceTask = loadTask(serviceTaskId);
        serviceTask.setStatus(ServiceTaskStatus.IN_PROGRESS);
        return serviceTaskRepository.save(serviceTask);
    }

    @Transactional
    public ServiceTask markTaskCompleted(Long serviceTaskId) {
        ServiceTask serviceTask = loadTask(serviceTaskId);
        serviceTask.setStatus(ServiceTaskStatus.COMPLETED);
        ServiceTask saved = serviceTaskRepository.save(serviceTask);
        notifyTaskCompleted(saved.getJobCard().getId());
        return saved;
    }

    public List<ServiceTask> getAllTasks() {
        return serviceTaskRepository.findAll();
    }

    public List<ServiceTask> getAssignedTasks(Long mechanicId) {
        return serviceTaskRepository.findAll().stream()
            .filter(task -> task.getMechanic() != null && task.getMechanic().getId().equals(mechanicId))
            .toList();
    }

    private ServiceTask loadTask(Long serviceTaskId) {
        return serviceTaskRepository.findById(serviceTaskId)
            .orElseThrow(() -> new IllegalArgumentException("ServiceTask not found: " + serviceTaskId));
    }

    @Override
    public void registerObserver(TaskCompletionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(TaskCompletionObserver observer) {
        observers.remove(observer);
    }

    // Observer Pattern: ServiceTask completion event is published to observers.
    // SOLID: follows DIP because MechanicService depends on observer abstraction.
    @Override
    public void notifyTaskCompleted(Long jobCardId) {
        for (TaskCompletionObserver observer : observers) {
            observer.onTaskCompleted(jobCardId);
        }
    }
}
