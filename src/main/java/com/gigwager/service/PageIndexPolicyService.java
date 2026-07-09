package com.gigwager.service;

import com.gigwager.model.CityData;
import com.gigwager.model.CityData.MarketTier;
import com.gigwager.model.CityIntentPage;
import com.gigwager.model.WorkLevel;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class PageIndexPolicyService {

    private static final Set<CityIntentPage> PRIORITY_INTENT_PAGES = EnumSet.of(
            CityIntentPage.AFTER_GAS,
            CityIntentPage.PER_MILE,
            CityIntentPage.ACTIVE_TIME,
            CityIntentPage.DAILY_100,
            CityIntentPage.HOURLY_PAY,
            CityIntentPage.HOW_MUCH_CAN_YOU_MAKE,
            CityIntentPage.BEST_AREAS,
            CityIntentPage.APP_COMPARISON,
            CityIntentPage.NIGHTS_WEEKENDS);

    private final CityRichContentRepository cityRichContentRepository;

    public PageIndexPolicyService(CityRichContentRepository cityRichContentRepository) {
        this.cityRichContentRepository = cityRichContentRepository;
    }

    /**
     * Quality Gate for City Base Reports (/salary/app/city)
     */
    public boolean isCityReportIndexable(CityData city) {
        return isCityReportIndexable(city, null);
    }

    public boolean isCityReportIndexable(CityData city, String app) {
        if (city == null || (app != null && !isKnownApp(app))) {
            return false;
        }
        // Enforce strict quality gates for indexing:
        // Do not index low tier cities, and demand cited local content to avoid thin
        // city permutations.
        if (city.getMarketTier() == MarketTier.LOW) {
            return false;
        }
        return cityRichContentRepository.hasRichCitedContent(city.getSlug());
    }

    /**
     * Quality Gate for Work-Level Reports (/salary/app/city/work-level)
     */
    public boolean isWorkLevelReportIndexable(CityData city, WorkLevel workLevel) {
        return isWorkLevelReportIndexable(city, workLevel, null);
    }

    public boolean isWorkLevelReportIndexable(CityData city, WorkLevel workLevel, String app) {
        if (workLevel == null || !isCityReportIndexable(city, app)) {
            return false;
        }
        return cityRichContentRepository.hasWorkLevelContent(city.getSlug(), workLevel.getSlug());
    }

    public boolean isCityIntentPageIndexable(CityData city, String app, CityIntentPage intentPage) {
        if (intentPage == null || !isKnownApp(app) || !intentPage.isSupportedForApp(app)) {
            return false;
        }
        if (!isCityReportIndexable(city, app)) {
            return false;
        }
        return PRIORITY_INTENT_PAGES.contains(intentPage);
    }

    private boolean isKnownApp(String app) {
        return "uber".equals(app) || "doordash".equals(app);
    }
}
