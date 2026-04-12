package com.vsms.customer.controller;

import com.vsms.customer.entity.AppointmentType;
import com.vsms.customer.service.CustomerService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("appointments", customerService.getAllAppointments());
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customer-dashboard";
    }

    @PostMapping("/book")
    public String bookAppointment(
        @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) AppointmentType appointmentType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDateTime,
        @RequestParam(required = false) String concernDescription,
        @RequestParam(required = false) String vehicleLicensePlate,
        @RequestParam(required = false) String serviceType,
        RedirectAttributes redirectAttributes
    ) {
        try {
            if (vehicleLicensePlate != null && !vehicleLicensePlate.isBlank() && serviceType != null && !serviceType.isBlank()) {
                customerService.bookAppointmentByVehicle(vehicleLicensePlate, serviceType);
            } else {
                if (customerId == null || appointmentType == null) {
                    throw new IllegalArgumentException("Either provide vehicleLicensePlate/serviceType or full appointment fields");
                }
                LocalDateTime effectiveDateTime = appointmentDateTime == null ? LocalDateTime.now().plusDays(1) : appointmentDateTime;
                String effectiveConcern = concernDescription == null ? "Booked via dashboard form" : concernDescription;
                customerService.bookAppointment(customerId, appointmentType, effectiveDateTime, effectiveConcern);
            }
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("bookingError", ex.getMessage());
        }
        return "redirect:/customer";
    }
}
