package com.vsms.customer.service.factory;

import com.vsms.customer.entity.Appointment;
import com.vsms.customer.entity.AppointmentType;
import com.vsms.customer.entity.Customer;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AppointmentFactory {

    private final Map<AppointmentType, AppointmentCreator> creators;

    public AppointmentFactory(
        GeneralAppointmentCreator generalAppointmentCreator,
        EmergencyAppointmentCreator emergencyAppointmentCreator,
        VipAppointmentCreator vipAppointmentCreator
    ) {
        this.creators = new EnumMap<>(AppointmentType.class);
        this.creators.put(AppointmentType.GENERAL, generalAppointmentCreator);
        this.creators.put(AppointmentType.EMERGENCY, emergencyAppointmentCreator);
        this.creators.put(AppointmentType.VIP, vipAppointmentCreator);
    }

    // Factory Pattern: encapsulates object construction and selects concrete creator at runtime.
    // SOLID: follows OCP because new appointment types can be added without changing client code.
    public Appointment createAppointment(
        AppointmentType type,
        Customer customer,
        LocalDateTime appointmentDateTime,
        String concernDescription
    ) {
        AppointmentCreator creator = creators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Unsupported appointment type: " + type);
        }
        return creator.create(customer, appointmentDateTime, concernDescription);
    }
}
