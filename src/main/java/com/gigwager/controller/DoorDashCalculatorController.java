package com.gigwager.controller;

import com.gigwager.service.VerdictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.gigwager.util.AppConstants;

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
        boolean hasInputs = gross != null && miles != null && hours != null;
        double grossVal = gross != null ? gross : 900.0;
        double milesVal = miles != null ? miles : 700.0;
        double hoursVal = hours != null ? hours : 35.0;
        double gasPriceVal = gasPrice != null ? gasPrice : 3.50;

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", grossVal);
        model.addAttribute("initialMiles", milesVal);
        model.addAttribute("initialHours", hoursVal);
        model.addAttribute("initialGasPrice", gasPriceVal);
        model.addAttribute("app", "doordash");

        // Calculate Verdict
        var verdict = verdictService.calculateVerdict(grossVal, milesVal, hoursVal, "DoorDash");

        double expenses = milesVal * AppConstants.IRS_MILEAGE_RATE;
        double profit = grossVal - expenses;
        double taxes = Math.max(0, profit * AppConstants.SELF_EMPLOYMENT_TAX_RATE);

        model.addAttribute("verdict", verdict);
        model.addAttribute("estimatedTaxes", taxes);
        model.addAttribute("estimatedVehicleCost", expenses);

        if (hasInputs) {
            model.addAttribute("customTitle",
                    "DoorDash Truth: I made $" + Math.round(grossVal) + "... but the real wage is shocking.");
        } else {
            model.addAttribute("customTitle", "DoorDash Driver Pay Calculator 2026: Real Net Hourly After Expenses");
        }

        return "pages/calculator";
    }
}
