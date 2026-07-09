package com.gigwager.controller;

import com.gigwager.service.DriverReportSubmissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class DriverReportController {

    private final DriverReportSubmissionService submissionService;

    public DriverReportController(DriverReportSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/driver-reports/submit")
    public String submitDriverReport(
            @RequestParam Map<String, String> form,
            RedirectAttributes redirectAttributes) {
        submissionService.savePending(form);
        redirectAttributes.addFlashAttribute("driverReportSubmitted", true);
        return "redirect:/driver-report-submitted";
    }

    @GetMapping("/driver-report-submitted")
    public String submitted(Model model) {
        model.addAttribute("noIndex", true);
        return "driver-report-submitted";
    }
}
