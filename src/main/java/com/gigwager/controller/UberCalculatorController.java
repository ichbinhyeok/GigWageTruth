package com.gigwager.controller;

import com.gigwager.service.VerdictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.gigwager.util.AppConstants;

@Controller
public class UberCalculatorController {

    private final VerdictService verdictService;

    public UberCalculatorController(VerdictService verdictService) {
        this.verdictService = verdictService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/uber")
    public String uber(@RequestParam(name = "gross", required = false) Double gross,
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
        model.addAttribute("app", "uber");

        // Calculate Verdict
        var verdict = verdictService.calculateVerdict(gross, miles, hours, "Uber");

        double expenses = miles * AppConstants.IRS_MILEAGE_RATE;
        double profit = gross - expenses;
        double taxes = Math.max(0, profit * AppConstants.SELF_EMPLOYMENT_TAX_RATE);

        model.addAttribute("verdict", verdict);
        model.addAttribute("estimatedTaxes", taxes);
        model.addAttribute("estimatedVehicleCost", expenses);

        // Dynamic SEO Title
        // Simple calculation for title context (approximate)
        model.addAttribute("customTitle", "I thought I made $" + gross.intValue() + "... The truth is shocking.");

        return "pages/calculator";
    }
}
