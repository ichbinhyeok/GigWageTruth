package com.gigwager.service;

import com.gigwager.model.CityData;
import com.gigwager.model.CityIntentPage;
import com.gigwager.model.CityScenario;
import com.gigwager.model.DriverShiftReport;
import com.gigwager.model.PageEvidenceProfile;
import com.gigwager.model.WorkLevel;
import com.gigwager.model.content.CityRichContent;
import com.gigwager.model.content.CitySeoData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageEvidenceService {

    private final CityRichContentRepository cityRichContentRepository;
    private final DriverShiftReportService driverShiftReportService;

    public PageEvidenceService(
            CityRichContentRepository cityRichContentRepository,
            DriverShiftReportService driverShiftReportService) {
        this.cityRichContentRepository = cityRichContentRepository;
        this.driverShiftReportService = driverShiftReportService;
    }

    public PageEvidenceProfile cityReport(
            String app,
            String appName,
            CityData city,
            CityScenario scenario,
            boolean indexable) {
        String anchor = String.format(
                "%s gas, %s weekly miles, %s weekly hours, IRS mileage, and SE tax are recomputed for this city.",
                currency(city.getGasPrice()),
                scenario.getMiles(),
                scenario.getHours());
        String heading = String.format("%s %s evidence check", city.getCityName(), appName);
        return build(app, city, heading, anchor, indexable);
    }

    public PageEvidenceProfile workLevelReport(
            String app,
            String appName,
            CityData city,
            WorkLevel workLevel,
            CityScenario scenario,
            boolean indexable) {
        String anchor = String.format(
                "%s uses its own %s-hour, %s-mile model plus local strategy notes for %s drivers.",
                workLevel.getDisplayName(),
                scenario.getHours(),
                scenario.getMiles(),
                city.getCityName());
        String heading = String.format("%s %s %s evidence check",
                city.getCityName(),
                appName,
                workLevel.getDisplayName());
        return build(app, city, heading, anchor, indexable);
    }

    public PageEvidenceProfile intentReport(
            String app,
            String appName,
            CityData city,
            CityIntentPage intentPage,
            CityScenario scenario,
            boolean indexable) {
        String heading = String.format("%s %s %s evidence check",
                city.getCityName(),
                appName,
                intentPage.getDisplayName());
        String anchor = intentAnchor(city, intentPage, scenario);
        return build(app, city, heading, anchor, indexable);
    }

    private PageEvidenceProfile build(
            String app,
            CityData city,
            String heading,
            String uniqueDataAnchor,
            boolean indexable) {
        Optional<CityRichContent> richContent = cityRichContentRepository == null
                ? Optional.empty()
                : cityRichContentRepository.findBySlug(city.getSlug());
        CitySeoData seo = richContent.map(CityRichContent::seo).orElse(null);
        int sourceCount = seo != null && seo.sources() != null ? seo.sources().size() : 0;
        boolean richCitedContent = sourceCount >= 2;
        String lastVerifiedAt = seo != null ? seo.lastVerifiedAt() : null;
        String methodologyVersion = seo != null ? seo.methodologyVersion() : "modeled-calculator-v1";

        List<DriverShiftReport> reports = driverShiftReportService == null
                ? List.of()
                : driverShiftReportService.getReportsForCity(app, city.getSlug());
        int driverReportCount = reports.size();
        int citySpecificDriverReportCount = (int) reports.stream()
                .filter(report -> !"benchmark".equals(report.citySlug()))
                .filter(report -> city.getSlug().equals(report.citySlug()))
                .count();

        String confidenceLabel;
        String confidenceTone;
        if (indexable && richCitedContent && citySpecificDriverReportCount > 0) {
            confidenceLabel = "Strong local evidence";
            confidenceTone = "strong";
        } else if (indexable && richCitedContent) {
            confidenceLabel = "Source-backed estimate";
            confidenceTone = "reviewed";
        } else if (citySpecificDriverReportCount > 0) {
            confidenceLabel = "Driver-evidence estimate";
            confidenceTone = "reviewed";
        } else {
            confidenceLabel = "Modeled only";
            confidenceTone = "thin";
        }

        String summary = buildSummary(indexable, richCitedContent, sourceCount, driverReportCount,
                citySpecificDriverReportCount);

        return new PageEvidenceProfile(
                heading,
                confidenceLabel,
                confidenceTone,
                summary,
                sourceCount,
                driverReportCount,
                citySpecificDriverReportCount,
                lastVerifiedAt,
                methodologyVersion,
                uniqueDataAnchor,
                richCitedContent,
                indexable);
    }

    private String buildSummary(
            boolean indexable,
            boolean richCitedContent,
            int sourceCount,
            int driverReportCount,
            int citySpecificDriverReportCount) {
        if (!indexable) {
            return "This page is available for users but is not submitted for indexing until the city has enough cited local evidence.";
        }
        if (richCitedContent && citySpecificDriverReportCount > 0) {
            return String.format(
                    "Indexed because it combines %d cited local sources with %d driver evidence item%s, including %d city-specific report%s.",
                    sourceCount,
                    driverReportCount,
                    plural(driverReportCount),
                    citySpecificDriverReportCount,
                    plural(citySpecificDriverReportCount));
        }
        if (richCitedContent) {
            return String.format(
                    "Indexed because it has %d cited local sources and a city-specific calculation model; driver reports are shown when available.",
                    sourceCount);
        }
        return "Published as a calculator fallback, but kept out of the sitemap until stronger local evidence is added.";
    }

    private String intentAnchor(CityData city, CityIntentPage intentPage, CityScenario scenario) {
        return switch (intentPage) {
            case AFTER_GAS -> String.format(
                    "Fuel sensitivity is recalculated from %s gas, %s weekly miles, and the current IRS mileage proxy.",
                    currency(city.getGasPrice()),
                    scenario.getMiles());
            case PER_MILE -> String.format(
                    "The offer floor is computed from %s weekly gross over %s modeled miles, then checked against net hourly.",
                    currency(scenario.getGrossWeekly()),
                    scenario.getMiles());
            case ACTIVE_TIME -> "Active-time pages separate logged work, unpaid waiting, route miles, and the modeled side-hustle baseline.";
            case DAILY_100 -> String.format(
                    "The $100/day target is converted into required hours, miles, and gross pay from a %s/hr net baseline.",
                    currency(scenario.getNetHourly()));
            case HOURLY_PAY -> String.format(
                    "Hourly pay is shown after mileage, gas, self-employment tax, and %s local market conditions.",
                    city.getCityName());
            case HOW_MUCH_CAN_YOU_MAKE -> "Daily, weekly, and hourly estimates are tied back to the same miles and tax assumptions instead of screenshot gross pay.";
            case BEST_AREAS -> String.format(
                    "Zone advice is constrained by %s traffic, airport, downtown, and restaurant-cluster friction.",
                    city.getCityName());
            case APP_COMPARISON -> "The comparison uses one city model so Uber Eats and DoorDash are judged on the same expense assumptions.";
            case NIGHTS_WEEKENDS -> "The schedule model focuses on dinner, weekend, and late-window demand instead of an all-day average.";
            case WORTH_IT -> "Worth-it pages compare net pay against vehicle wear, schedule risk, and local cost pressure.";
            case MONTHLY_1000 -> "The monthly target is broken into repeatable weekly hours, miles, gross pay, and net-profit math.";
        };
    }

    private String currency(double value) {
        return String.format("$%.2f", value);
    }

    private String plural(int count) {
        return count == 1 ? "" : "s";
    }
}
