package com.vsms.customer.repository;

import com.vsms.customer.entity.Appointment;
import com.vsms.customer.entity.AppointmentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByStatus(AppointmentStatus status);
}
