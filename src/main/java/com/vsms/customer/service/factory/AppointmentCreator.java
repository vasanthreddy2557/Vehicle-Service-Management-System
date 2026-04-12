package com.vsms.customer.service.factory;

import com.vsms.customer.entity.Appointment;
import com.vsms.customer.entity.Customer;
import java.time.LocalDateTime;

public interface AppointmentCreator {

    Appointment create(Customer customer, LocalDateTime appointmentDateTime, String concernDescription);
}
