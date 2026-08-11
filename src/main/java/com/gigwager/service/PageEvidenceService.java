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
        String heading = String.format("How this %s %s estimate was built", city.getCityName(), appName);
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
        String heading = String.format("How this %s %s %s estimate was built",
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
        String heading = String.format("How this %s %s %s estimate was built",
                city.getCityName(),
                appName,
                intentPage.getDisplayName());
        String anchor = intentAnchor(city, intentPage, scenario);
        return build(app, city, heading, anchor, indexable);
    }

    public PageEvidenceProfile comparisonReport(
            CityData city,
            CityScenario uberScenario,
            CityScenario doordashScenario,
            String winningAppName,
            double netHourlyGap,
            boolean indexable) {
        String heading = String.format("How this %s Uber Eats vs DoorDash comparison was built", city.getCityName());
        String anchor = String.format(
                "Both apps use the same %s-hour, %s-mile side-hustle baseline, %s local gas context, IRS mileage proxy, and SE tax assumption; the modeled gap is %s/hr net.",
                uberScenario.getHours(),
                uberScenario.getMiles(),
                currency(city.getGasPrice()),
                currency(netHourlyGap));
        if (netHourlyGap < 0.25) {
            anchor = String.format(
                    "Both apps use the same %s-hour, %s-mile side-hustle baseline, %s local gas context, IRS mileage proxy, and SE tax assumption; the current model is effectively tied.",
                    uberScenario.getHours(),
                    uberScenario.getMiles(),
                    currency(city.getGasPrice()));
        }
        PageEvidenceProfile base = build("doordash", city, heading, anchor, indexable);
        String summary = indexable
                ? String.format(
                        "This comparison uses the same cited local inputs for both apps and shows them side by side. Current model result: %s.",
                        netHourlyGap < 0.25 ? "effectively tied" : winningAppName)
                : base.summary();
        return new PageEvidenceProfile(
                base.heading(),
                base.confidenceLabel(),
                base.confidenceTone(),
                summary,
                base.sourceCount(),
                base.driverReportCount(),
                base.citySpecificDriverReportCount(),
                base.lastVerifiedAt(),
                base.methodologyVersion(),
                base.uniqueDataAnchor(),
                base.richCitedContent(),
                base.indexable());
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
            return "This is a modeled estimate with limited local evidence. Adjust the inputs and treat the result as a planning range, not a guaranteed wage.";
        }
        if (richCitedContent && citySpecificDriverReportCount > 0) {
            return String.format(
                    "This estimate combines %d cited local sources with %d driver evidence item%s, including %d city-specific report%s.",
                    sourceCount,
                    driverReportCount,
                    plural(driverReportCount),
                    citySpecificDriverReportCount,
                    plural(citySpecificDriverReportCount));
        }
        if (richCitedContent) {
            return String.format(
                    "This estimate uses %d cited source%s for local context. No city-specific driver report is included yet; %d broader app benchmark%s are shown separately.",
                    sourceCount,
                    plural(sourceCount),
                    driverReportCount,
                    plural(driverReportCount));
        }
        return "This is a calculator-based estimate. Local driver evidence has not yet been added, so adjust the inputs before relying on the result.";
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
