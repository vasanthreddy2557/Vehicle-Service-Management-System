package com.vsms.advisor.controller;

import com.vsms.advisor.service.AdvisorService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/advisor")
public class AdvisorController {

    private final AdvisorService advisorService;

    public AdvisorController(AdvisorService advisorService) {
        this.advisorService = advisorService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("appointments", advisorService.getPendingAppointments());
        model.addAttribute("jobCards", advisorService.getAllJobCards());
        return "advisor-dashboard";
    }

    @PostMapping("/job/{id}/start")
    public String startJob(@PathVariable Long id) {
        advisorService.startJob(id);
        return "redirect:/advisor";
    }

    @PostMapping("/job/{id}/qa")
    public String requestQa(@PathVariable Long id) {
        advisorService.requestQa(id);
        return "redirect:/advisor";
    }

    @PostMapping("/job/create")
    public String createJobCard(
        @RequestParam Long appointmentId,
        @RequestParam Long serviceAdvisorId,
        @RequestParam(required = false) BigDecimal laborEstimate,
        @RequestParam(required = false) String notes
    ) {
        advisorService.createJobCard(appointmentId, serviceAdvisorId, laborEstimate, notes);
        return "redirect:/advisor";
    }
}
