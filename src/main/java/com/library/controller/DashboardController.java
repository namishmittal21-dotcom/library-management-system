package com.library.controller;

import com.library.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", dashboardService.getTotalBooks());
        model.addAttribute("totalMembers", dashboardService.getTotalMembers());
        model.addAttribute("booksIssued", dashboardService.getBooksIssued());
        model.addAttribute("overdueBooks", dashboardService.getOverdueBooks());
        return "dashboard";
    }
}