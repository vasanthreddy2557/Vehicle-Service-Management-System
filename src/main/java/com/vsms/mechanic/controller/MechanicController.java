package com.vsms.mechanic.controller;

import com.vsms.mechanic.service.MechanicService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mechanic")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("tasks", mechanicService.getAllTasks());
        return "mechanic-dashboard";
    }

    @PostMapping("/task/{id}/complete")
    public String completeTask(@PathVariable Long id) {
        mechanicService.markTaskCompleted(id);
        return "redirect:/mechanic";
    }

    @PostMapping("/task/create")
    public String createTask(
        @RequestParam Long jobCardId,
        @RequestParam Long mechanicId,
        @RequestParam String taskName,
        @RequestParam(required = false) String taskDescription,
        @RequestParam(required = false) BigDecimal laborCost
    ) {
        mechanicService.assignTask(jobCardId, mechanicId, taskName, taskDescription, laborCost);
        return "redirect:/mechanic";
    }
}
