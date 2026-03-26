package com.vsms.vehicle_service_system.customer.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apptId;

    private Long customerId; // Links to the User table
    private String vehicleLicensePlate;
    private LocalDateTime scheduledDate;
    private String serviceType;
    private String status; // DRAFT, PENDING_APPROVAL, CONFIRMED

    // TODO: Right-click -> Generate -> Getters and Setters
}