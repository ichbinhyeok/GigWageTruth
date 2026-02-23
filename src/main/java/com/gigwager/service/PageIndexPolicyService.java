package com.gigwager.service;

import com.gigwager.model.CityData;
import com.gigwager.model.CityData.MarketTier;
import com.gigwager.model.WorkLevel;
import org.springframework.stereotype.Service;

@Service
public class PageIndexPolicyService {

    private final DataLayerService dataLayerService;

    public PageIndexPolicyService(DataLayerService dataLayerService) {
        this.dataLayerService = dataLayerService;
    }

    /**
     * Quality Gate for City Base Reports (/salary/app/city)
     */
    public boolean isCityReportIndexable(CityData city) {
        // Enforce strict quality gates for indexing:
        // Do not index low tier cities, and demand rich programmatic data to avoid thin
        // content
        if (city.getMarketTier() == MarketTier.LOW) {
            return false;
        }
        return dataLayerService.hasRichLocalData(city.getSlug());
    }

    /**
     * Quality Gate for Work-Level Reports (/salary/app/city/work-level)
     */
    public boolean isWorkLevelReportIndexable(CityData city, WorkLevel workLevel) {
        // Rule: Index only SIDE_HUSTLE (featured) scenarios for major cities
        if (!isCityReportIndexable(city)) {
            return false;
        }
        return workLevel == WorkLevel.SIDE_HUSTLE;
    }
}
