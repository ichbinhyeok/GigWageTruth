package com.gigwager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UberCalculatorController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/uber")
    public String uber(@RequestParam(name = "gross", required = false) Double gross,
            @RequestParam(name = "miles", required = false) Double miles,
            @RequestParam(name = "hours", required = false) Double hours,
            Model model) {

        // Strict 2-Page Flow: If no params, go back to Gateway (Index)
        if (gross == null || miles == null || hours == null) {
            return "redirect:/";
        }

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", gross);
        model.addAttribute("initialMiles", miles);
        model.addAttribute("initialHours", hours);
        model.addAttribute("app", "uber");

        // Dynamic SEO Title
        // Simple calculation for title context (approximate)
        model.addAttribute("customTitle", "I thought I made $" + gross.intValue() + "... The truth is shocking.");

        return "pages/calculator";
    }
}
