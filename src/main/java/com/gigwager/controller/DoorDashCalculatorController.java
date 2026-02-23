package com.gigwager.controller;

import com.gigwager.service.VerdictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DoorDashCalculatorController {

    private final VerdictService verdictService;

    public DoorDashCalculatorController(VerdictService verdictService) {
        this.verdictService = verdictService;
    }

    @GetMapping("/doordash")
    public String doordash(@RequestParam(name = "gross", required = false) Double gross,
            @RequestParam(name = "miles", required = false) Double miles,
            @RequestParam(name = "hours", required = false) Double hours,
            @RequestParam(name = "gasPrice", required = false) Double gasPrice,
            Model model) {

        // Strict 2-Page Flow: If no params, go back to Gateway (Index)
        if (gross == null || miles == null || hours == null) {
            return "redirect:/";
        }

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", gross);
        model.addAttribute("initialMiles", miles);
        model.addAttribute("initialHours", hours);
        model.addAttribute("initialGasPrice", gasPrice);
        model.addAttribute("app", "doordash");

        // Calculate Verdict
        double grossVal = gross;
        double milesVal = miles;
        double hoursVal = hours;

        var verdict = verdictService.calculateVerdict(grossVal, milesVal, hoursVal, "DoorDash");

        double expenses = milesVal * 0.725;
        double profit = grossVal - expenses;
        double taxes = Math.max(0, profit * 0.153);

        model.addAttribute("verdict", verdict);
        model.addAttribute("estimatedTaxes", taxes);
        model.addAttribute("estimatedVehicleCost", expenses);

        // Dynamic SEO Title
        // Simple calculation for title context (approximate)
        model.addAttribute("customTitle",
                "DoorDash Truth: I made $" + gross.intValue() + "... but the real wage is shocking.");

        return "pages/calculator";
    }
}
