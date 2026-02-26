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
        boolean hasInputs = gross != null && miles != null && hours != null;
        double grossVal = gross != null ? gross : 1000.0;
        double milesVal = miles != null ? miles : 800.0;
        double hoursVal = hours != null ? hours : 40.0;
        double gasPriceVal = gasPrice != null ? gasPrice : 3.50;

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", grossVal);
        model.addAttribute("initialMiles", milesVal);
        model.addAttribute("initialHours", hoursVal);
        model.addAttribute("initialGasPrice", gasPriceVal);
        model.addAttribute("app", "uber");

        // Calculate Verdict
        var verdict = verdictService.calculateVerdict(grossVal, milesVal, hoursVal, "Uber");

        double expenses = milesVal * AppConstants.IRS_MILEAGE_RATE;
        double profit = grossVal - expenses;
        double taxes = Math.max(0, profit * AppConstants.SELF_EMPLOYMENT_TAX_RATE);

        model.addAttribute("verdict", verdict);
        model.addAttribute("estimatedTaxes", taxes);
        model.addAttribute("estimatedVehicleCost", expenses);

        if (hasInputs) {
            model.addAttribute("customTitle",
                    "I thought I made $" + Math.round(grossVal) + "... The truth is shocking.");
        } else {
            model.addAttribute("customTitle", "Uber Driver Pay Calculator 2026: Real Hourly Wage After Expenses");
        }

        return "pages/calculator";
    }
}
