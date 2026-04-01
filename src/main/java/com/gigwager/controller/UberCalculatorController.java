package com.gigwager.controller;

import com.gigwager.model.GigCalculationRequest;
import com.gigwager.service.VerdictService;
import com.gigwager.service.GigCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UberCalculatorController {

    private final VerdictService verdictService;
    private final GigCalculationService gigCalculationService;

    public UberCalculatorController(VerdictService verdictService, GigCalculationService gigCalculationService) {
        this.verdictService = verdictService;
        this.gigCalculationService = gigCalculationService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/uber")
    public String uber(@RequestParam(name = "gross", required = false) Double gross,
            @RequestParam(name = "miles", required = false) Double miles,
            @RequestParam(name = "hours", required = false) Double hours,
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
            Model model) {
        boolean hasInputs = gross != null && miles != null && hours != null;
        GigCalculationRequest calculationRequest = GigCalculationRequest.fromInputs(
                gross != null ? gross : 1000.0,
                miles != null ? miles : 800.0,
                hours != null ? hours : 40.0,
                tips,
                bonuses,
                activeTime,
                gasPrice != null ? gasPrice : 3.50,
                taxRate,
                roundTrip,
                calculationMode,
                vehicleId != null ? vehicleId : "prius",
                customMpg,
                customMaintenance,
                customDepreciation);
        var calculationResult = gigCalculationService.calculate(calculationRequest);

        // Pass params to view for Alpine JS initialization
        model.addAttribute("initialGross", calculationRequest.gross());
        model.addAttribute("initialMiles", calculationRequest.miles());
        model.addAttribute("initialHours", calculationRequest.hours());
        model.addAttribute("initialTips", calculationRequest.tips());
        model.addAttribute("initialBonuses", calculationRequest.bonuses());
        model.addAttribute("initialActiveTime", calculationRequest.activeTime());
        model.addAttribute("initialGasPrice", calculationRequest.gasPrice());
        model.addAttribute("initialTaxRate", calculationRequest.taxRate());
        model.addAttribute("initialRoundTrip", calculationRequest.roundTrip());
        model.addAttribute("initialCalculationMode", calculationRequest.calculationMode());
        model.addAttribute("initialSelectedVehicleId", calculationRequest.vehicleId());
        model.addAttribute("initialCustomMpg", calculationRequest.customMpg());
        model.addAttribute("initialCustomMaintenance", calculationRequest.customMaintenance());
        model.addAttribute("initialCustomDepreciation", calculationRequest.customDepreciation());
        model.addAttribute("app", "uber");
        model.addAttribute("calculationResult", calculationResult);

        model.addAttribute("verdict", verdictService.calculateVerdict(calculationResult, "Uber"));

        if (hasInputs) {
            model.addAttribute("customTitle",
                    "What I actually kept from $" + Math.round(calculationResult.totalEarnings()) + " on Uber this week");
        } else {
            model.addAttribute("customTitle", "Uber Weekly Take-Home Calculator 2026");
        }

        return "pages/calculator";
    }
}
