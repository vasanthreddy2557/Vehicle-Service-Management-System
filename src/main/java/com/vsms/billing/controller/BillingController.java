package com.vsms.billing.controller;

import com.vsms.billing.service.BillingService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("jobCards", billingService.getReadyForBillingJobCards());
        model.addAttribute("invoices", billingService.getAllInvoices());
        return "billing-dashboard";
    }

    @PostMapping("/generate/{jobId}")
    public String generateInvoice(
        @PathVariable Long jobId,
        @RequestParam(required = false) BigDecimal taxRate,
        @RequestParam Map<String, String> requestParams
    ) {
        Map<Long, Integer> partQuantities = extractPartQuantities(requestParams);
        billingService.generateInvoice(jobId, partQuantities, taxRate);
        return "redirect:/billing";
    }

    private Map<Long, Integer> extractPartQuantities(Map<String, String> requestParams) {
        Map<Long, Integer> partQuantities = new HashMap<>();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if (entry.getKey().startsWith("part_")) {
                Long partId = Long.valueOf(entry.getKey().substring("part_".length()));
                partQuantities.put(partId, Integer.valueOf(entry.getValue()));
            }
        }
        return partQuantities;
    }
}
