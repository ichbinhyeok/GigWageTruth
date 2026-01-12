package com.gigwager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UberCalculatorController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/uber")
    public String uber(@RequestParam(required = false) Double gross,
            @RequestParam(required = false) Double miles,
            @RequestParam(required = false) Double hours,
            Model model) {

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", gross != null ? gross : 1000);
        model.addAttribute("initialMiles", miles != null ? miles : 800);
        model.addAttribute("initialHours", hours != null ? hours : 40);

        // Dynamic SEO Title
        if (gross != null && miles != null && hours != null) {
            // Simple calculation for title context (approximate)
            // Note: Exact calc happens in JS, this is just for the hook.
            model.addAttribute("customTitle", "I thought I made $" + gross.intValue() + "... The truth is shocking.");
        }

        return "uber";
    }
}
