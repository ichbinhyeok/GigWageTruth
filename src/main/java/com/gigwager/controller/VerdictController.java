package com.gigwager.controller;

import com.gigwager.model.GigCalculationRequest;
import com.gigwager.model.Verdict;
import com.gigwager.service.GigCalculationService;
import com.gigwager.service.VerdictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerdictController {

    private final VerdictService verdictService;
    private final GigCalculationService gigCalculationService;

    @Autowired
    public VerdictController(VerdictService verdictService, GigCalculationService gigCalculationService) {
        this.verdictService = verdictService;
        this.gigCalculationService = gigCalculationService;
    }

    @GetMapping("/api/verdict-fragment")
    public String getVerdictFragment(
            @RequestParam(name = "gross", defaultValue = "0") Double gross,
            @RequestParam(name = "miles", defaultValue = "0") Double miles,
            @RequestParam(name = "hours", defaultValue = "0") Double hours,
            @RequestParam(name = "tips", required = false) Double tips,
            @RequestParam(name = "bonuses", required = false) Double bonuses,
            @RequestParam(name = "activeTime", required = false) Double activeTime,
            @RequestParam(name = "gasPrice", required = false) Double gasPrice,
            @RequestParam(name = "taxRate", required = false) Double taxRate,
            @RequestParam(name = "roundTrip", required = false) Boolean roundTrip,
            @RequestParam(name = "calculationMode", required = false) String calculationMode,
            @RequestParam(name = "vehicleId", required = false) String vehicleId,
            @RequestParam(name = "customMpg", required = false) Double customMpg,
            @RequestParam(name = "customMaintenance", required = false) Double customMaintenance,
            @RequestParam(name = "customDepreciation", required = false) Double customDepreciation,
            @RequestParam(name = "app", defaultValue = "Uber") String app,
            Model model) {

        GigCalculationRequest calculationRequest = GigCalculationRequest.fromInputs(
                gross,
                miles,
                hours,
                tips,
                bonuses,
                activeTime,
                gasPrice,
                taxRate,
                roundTrip,
                calculationMode,
                vehicleId,
                customMpg,
                customMaintenance,
                customDepreciation);
        var calculationResult = gigCalculationService.calculate(calculationRequest);
        Verdict verdict = verdictService.calculateVerdict(calculationResult, app);

        // Add to model for JTE rendering
        model.addAttribute("verdict", verdict);
        model.addAttribute("calculationResult", calculationResult);

        // Return the fragment template directly
        return "components/verdict_card";
    }
}
