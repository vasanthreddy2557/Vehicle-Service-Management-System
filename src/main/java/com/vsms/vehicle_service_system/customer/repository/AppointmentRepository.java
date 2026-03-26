package com.vsms.vehicle_service_system.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vsms.vehicle_service_system.customer.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Custom method to find appointments for a specific customer
    List<Appointment> findByCustomerId(Long customerId);
}
