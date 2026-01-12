package com.gigwager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DoorDashCalculatorController {

    @GetMapping("/doordash")
    public String doordash(@RequestParam(required = false) Double gross,
            @RequestParam(required = false) Double miles,
            @RequestParam(required = false) Double hours,
            Model model) {

        // Strict 2-Page Flow: If no params, go back to Gateway (Index)
        if (gross == null || miles == null || hours == null) {
            return "redirect:/";
        }

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", gross);
        model.addAttribute("initialMiles", miles);
        model.addAttribute("initialHours", hours);
        model.addAttribute("app", "doordash");

        // Dynamic SEO Title
        // Simple calculation for title context (approximate)
        model.addAttribute("customTitle",
                "DoorDash Truth: I made $" + gross.intValue() + "... but the real wage is shocking.");

        return "calculator";
    }
}
