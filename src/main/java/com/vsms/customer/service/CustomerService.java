package com.vsms.customer.service;

import com.vsms.customer.entity.Appointment;
import com.vsms.customer.entity.AppointmentType;
import com.vsms.customer.entity.Customer;
import com.vsms.customer.repository.AppointmentRepository;
import com.vsms.customer.repository.CustomerRepository;
import com.vsms.customer.service.factory.AppointmentFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentFactory appointmentFactory;

    public CustomerService(
        CustomerRepository customerRepository,
        AppointmentRepository appointmentRepository,
        AppointmentFactory appointmentFactory
    ) {
        this.customerRepository = customerRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentFactory = appointmentFactory;
    }

    @Transactional
    public Appointment bookAppointment(
        Long customerId,
        AppointmentType appointmentType,
        LocalDateTime appointmentDateTime,
        String concernDescription
    ) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        Appointment appointment = appointmentFactory.createAppointment(
            appointmentType,
            customer,
            appointmentDateTime,
            concernDescription
        );

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment bookAppointmentByVehicle(String vehicleLicensePlate, String serviceType) {
        Customer customer = customerRepository.findByVehicleNumber(vehicleLicensePlate)
            .orElseGet(() -> customerRepository.save(createGuestCustomerForVehicle(vehicleLicensePlate)));

        AppointmentType appointmentType = resolveSupportedAppointmentType(serviceType);

        Appointment appointment = appointmentFactory.createAppointment(
            appointmentType,
            customer,
            LocalDateTime.now().plusDays(1),
            "Booked via dashboard form"
        );

        return appointmentRepository.save(appointment);
    }

    private AppointmentType resolveSupportedAppointmentType(String serviceType) {
        if (serviceType == null || serviceType.isBlank()) {
            return AppointmentType.GENERAL;
        }

        String normalized = serviceType.trim().toUpperCase(Locale.ROOT);

        // Current DB schema supports GENERAL and EMERGENCY only.
        if ("VIP".equals(normalized)) {
            return AppointmentType.GENERAL;
        }

        try {
            AppointmentType type = AppointmentType.valueOf(normalized);
            return type == AppointmentType.VIP ? AppointmentType.GENERAL : type;
        } catch (IllegalArgumentException ex) {
            return AppointmentType.GENERAL;
        }
    }

    private Customer createGuestCustomerForVehicle(String vehicleLicensePlate) {
        String normalizedPlate = vehicleLicensePlate.trim().toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");

        Customer customer = new Customer();
        customer.setFullName("Walk-in Customer " + normalizedPlate);
        customer.setEmail(normalizedPlate.toLowerCase(Locale.ROOT) + "@vsms.local");
        customer.setPhoneNumber("TEMP-" + Math.abs(normalizedPlate.hashCode()));
        customer.setVehicleNumber(normalizedPlate);
        return customer;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
