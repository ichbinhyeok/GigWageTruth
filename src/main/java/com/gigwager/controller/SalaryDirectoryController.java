package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.service.PageIndexPolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SalaryDirectoryController {

    private final PageIndexPolicyService pageIndexPolicyService;

    public SalaryDirectoryController(PageIndexPolicyService pageIndexPolicyService) {
        this.pageIndexPolicyService = pageIndexPolicyService;
    }

    @GetMapping("/salary/directory")
    public String directory(Model model) {
        Set<String> comparableCitySlugs = java.util.Arrays.stream(CityData.values())
                .filter(city -> pageIndexPolicyService.isCityReportIndexable(city, "uber"))
                .filter(city -> pageIndexPolicyService.isCityReportIndexable(city, "doordash"))
                .map(CityData::getSlug)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        model.addAttribute("comparableCitySlugs", comparableCitySlugs);
        return "salary/directory";
    }
}
