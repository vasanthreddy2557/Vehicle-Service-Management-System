package com.vsms.customer.service.factory;

import com.vsms.customer.entity.Appointment;
import com.vsms.customer.entity.AppointmentStatus;
import com.vsms.customer.entity.AppointmentType;
import com.vsms.customer.entity.Customer;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class VipAppointmentCreator implements AppointmentCreator {

    @Override
    public Appointment create(Customer customer, LocalDateTime appointmentDateTime, String concernDescription) {
        return new Appointment(
            null,
            customer,
            AppointmentType.VIP,
            AppointmentStatus.CONFIRMED,
            appointmentDateTime,
            concernDescription
        );
    }
}
