package com.gigwager.controller;

import com.gigwager.model.Verdict;
import com.gigwager.service.VerdictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerdictController {

    private final VerdictService verdictService;

    @Autowired
    public VerdictController(VerdictService verdictService) {
        this.verdictService = verdictService;
    }

    @GetMapping("/api/verdict-fragment")
    public String getVerdictFragment(
            @RequestParam(name = "gross", defaultValue = "0") Double gross,
            @RequestParam(name = "miles", defaultValue = "0") Double miles,
            @RequestParam(name = "hours", defaultValue = "0") Double hours,
            @RequestParam(name = "app", defaultValue = "Uber") String app,
            Model model) {

        // Calculate Verdict
        Verdict verdict = verdictService.calculateVerdict(gross, miles, hours, app);

        // Calculate simple costs for CTA Triggers
        double expenses = miles * 0.725; // 2026 IRS Rate
        double profit = gross - expenses;
        double taxes = Math.max(0, profit * 0.153);

        // Add to model for JTE rendering
        model.addAttribute("verdict", verdict);
        model.addAttribute("estimatedTaxes", taxes);
        model.addAttribute("estimatedVehicleCost", expenses);

        // Return the fragment template directly
        return "components/verdict_card";
    }
}
