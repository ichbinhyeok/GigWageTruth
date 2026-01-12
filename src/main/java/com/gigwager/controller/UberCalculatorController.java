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
    public String uber(Model model) {
        // Since the interactive logic is client-side implementation (Alpine.js) for
        // dwell time,
        // we just render the template. The layout handles the defaults.
        return "uber";
    }
}
