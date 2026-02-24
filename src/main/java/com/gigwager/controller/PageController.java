package com.gigwager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/methodology")
    public String methodology(Model model) {
        model.addAttribute("customTitle", "Methodology - How We Calculate Real Wages");
        return "methodology";
    }

    // --- Phase 2: Intent Hub Landing Pages ---
    @GetMapping("/uber-after-expenses")
    public String uberAfterExpenses(Model model) {
        model.addAttribute("customTitle", "Uber After Expenses 2026: The Real Hourly Wage");
        return "hubs/uber-after-expenses";
    }

    @GetMapping("/doordash-after-expenses")
    public String doordashAfterExpenses(Model model) {
        model.addAttribute("customTitle", "DoorDash After Expenses 2026: Real Net Profit");
        return "hubs/doordash-after-expenses";
    }

    @GetMapping("/net-hourly-calculator")
    public String netHourlyCalculator(Model model) {
        model.addAttribute("customTitle", "Gig Worker Net Hourly Calculator 2026");
        return "hubs/net-hourly-calculator";
    }

    @GetMapping("/multi-apping")
    public String multiApping(Model model) {
        model.addAttribute("customTitle", "Mastering Multi-Apping: Uber & DoorDash Strategy");
        return "hubs/multi-apping";
    }
    // --- End Phase 2 ---

    // --- Phase 7: Core Revenue-Intent Clusters ---

    @GetMapping("/taxes")
    public String taxes(Model model) {
        model.addAttribute("customTitle", "Gig Worker Tax Guide 2026: Don't Overpay the IRS");
        return "clusters/taxes";
    }

    @GetMapping("/insurance")
    public String insurance(Model model) {
        model.addAttribute("customTitle", "Rideshare & Delivery Insurance Guide 2026");
        return "clusters/insurance";
    }

    @GetMapping("/vehicle-cost")
    public String vehicleCost(Model model) {
        model.addAttribute("customTitle", "True Cost of Driving: Depreciation, Gas, & Maintenance");
        return "clusters/vehicle-cost";
    }

    @GetMapping("/taxes/quarterly-estimator")
    public String quarterlyEstimator(Model model) {
        model.addAttribute("customTitle", "Quarterly Tax Estimator - Avoid IRS Penalties");
        return "clusters/quarterly-estimator";
    }

    @GetMapping("/insurance/rideshare-basics")
    public String rideshareBasics(Model model) {
        model.addAttribute("customTitle", "Rideshare Insurance Basics - Do You Need It?");
        return "clusters/rideshare-basics";
    }

    @GetMapping("/vehicle-cost/cost-per-mile")
    public String costPerMile(Model model) {
        model.addAttribute("customTitle", "Calculate Your True Cost Per Mile ($/mi)");
        return "clusters/cost-per-mile";
    }

    // --- End Phase 7 ---

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("customTitle", "Gig Driver Blog - Strategies & Truths");
        return "blog/index";
    }

    @GetMapping("/blog/multi-apping-guide")
    public String multiAppingGuide(Model model) {
        model.addAttribute("customTitle", "Multi-Apping Guide 2026: Double Your Hourly Wage");
        return "blog/multi-apping-guide";
    }

    @GetMapping("/blog/tax-guide")
    public String taxGuide(Model model) {
        model.addAttribute("customTitle", "Gig Worker Tax Guide 2026: Don't Overpay the IRS");
        return "blog/tax-guide";
    }

    @GetMapping("/blog/uber-vs-doordash")
    public String uberVsDoordash(Model model) {
        model.addAttribute("customTitle", "Uber vs DoorDash 2026: Which App Pays More?");
        return "blog/uber-vs-doordash";
    }

    @GetMapping("/blog/hidden-costs")
    public String hiddenCosts(Model model) {
        model.addAttribute("customTitle", "The Hidden Cost of Depreciation: Gig Worker Reality");
        return "blog/hidden-costs";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("customTitle", "About Us - The GigWageTruth Story");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("customTitle", "Contact Us");
        return "contact";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("customTitle", "Privacy Policy");
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("customTitle", "Terms of Service");
        return "terms";
    }
}
