package com.vsms.advisor.service;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import com.vsms.advisor.entity.ServiceAdvisor;
import com.vsms.advisor.repository.JobCardRepository;
import com.vsms.advisor.repository.ServiceAdvisorRepository;
import com.vsms.advisor.service.state.JobCardState;
import com.vsms.advisor.service.state.JobCardStateFactory;
import com.vsms.customer.entity.Appointment;
import com.vsms.customer.repository.AppointmentRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdvisorService {

    private final JobCardRepository jobCardRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceAdvisorRepository serviceAdvisorRepository;
    private final JobCardStateFactory jobCardStateFactory;

    public AdvisorService(
        JobCardRepository jobCardRepository,
        AppointmentRepository appointmentRepository,
        ServiceAdvisorRepository serviceAdvisorRepository,
        JobCardStateFactory jobCardStateFactory
    ) {
        this.jobCardRepository = jobCardRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceAdvisorRepository = serviceAdvisorRepository;
        this.jobCardStateFactory = jobCardStateFactory;
    }

    @Transactional
    public JobCard createJobCard(Long appointmentId, Long serviceAdvisorId, BigDecimal laborEstimate, String notes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));

        ServiceAdvisor serviceAdvisor = serviceAdvisorRepository.findById(serviceAdvisorId)
            .orElseThrow(() -> new IllegalArgumentException("Service advisor not found: " + serviceAdvisorId));

        JobCard jobCard = new JobCard();
        jobCard.setAppointment(appointment);
        jobCard.setCustomer(appointment.getCustomer());
        jobCard.setServiceAdvisor(serviceAdvisor);
        jobCard.setStatus(JobCardStatus.CREATED);
        jobCard.setLaborCostEstimate(laborEstimate);
        jobCard.setTechnicianNotes(notes);

        return jobCardRepository.save(jobCard);
    }

    @Transactional
    public JobCard startJob(Long jobCardId) {
        JobCard jobCard = loadJobCard(jobCardId);
        resolveState(jobCard).startJob(jobCard);
        return jobCardRepository.save(jobCard);
    }

    @Transactional
    public JobCard requestQa(Long jobCardId) {
        JobCard jobCard = loadJobCard(jobCardId);
        resolveState(jobCard).requestQa(jobCard);
        return jobCardRepository.save(jobCard);
    }

    @Transactional
    public JobCard markReadyForBilling(Long jobCardId) {
        JobCard jobCard = loadJobCard(jobCardId);
        resolveState(jobCard).markReadyForBilling(jobCard);
        return jobCardRepository.save(jobCard);
    }

    public List<Appointment> getPendingAppointments() {
        return appointmentRepository.findByStatus(com.vsms.customer.entity.AppointmentStatus.REQUESTED);
    }

    public List<JobCard> getAllJobCards() {
        return jobCardRepository.findAll();
    }

    private JobCard loadJobCard(Long jobCardId) {
        return jobCardRepository.findById(jobCardId)
            .orElseThrow(() -> new IllegalArgumentException("JobCard not found: " + jobCardId));
    }

    // State Pattern: behavior changes by current state object instead of if-else chains.
    // SOLID: follows SRP and OCP because each state owns one transition rule-set.
    private JobCardState resolveState(JobCard jobCard) {
        return jobCardStateFactory.getState(jobCard.getStatus());
    }
}
