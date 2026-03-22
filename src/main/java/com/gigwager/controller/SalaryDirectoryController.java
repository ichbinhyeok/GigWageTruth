package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.PageIndexPolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SalaryDirectoryController {

    private final DataLayerService dataLayerService;
    private final PageIndexPolicyService pageIndexPolicyService;

    public SalaryDirectoryController(DataLayerService dataLayerService,
            PageIndexPolicyService pageIndexPolicyService) {
        this.dataLayerService = dataLayerService;
        this.pageIndexPolicyService = pageIndexPolicyService;
    }

    @GetMapping("/salary/directory")
    public String directory(Model model) {
        Set<String> comparableCitySlugs = java.util.Arrays.stream(CityData.values())
                .filter(pageIndexPolicyService::isCityReportIndexable)
                .map(CityData::getSlug)
                .filter(dataLayerService::hasRichLocalData)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        model.addAttribute("comparableCitySlugs", comparableCitySlugs);
        return "salary/directory";
    }
}
