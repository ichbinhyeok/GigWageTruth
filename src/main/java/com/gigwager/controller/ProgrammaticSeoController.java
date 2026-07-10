package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityEarningsSnapshot;
import com.gigwager.model.CityIntentEvidence;
import com.gigwager.model.CityIntentMetric;
import com.gigwager.model.CityIntentPage;
import com.gigwager.model.CityScenario;
import com.gigwager.model.DoorDashAdjustmentScenario;
import com.gigwager.model.DoorDashDurationEstimate;
import com.gigwager.model.DoorDashMoneyIntent;
import com.gigwager.model.DriverFieldNote;
import com.gigwager.model.DriverShiftReport;
import com.gigwager.model.PageEvidenceProfile;
import com.gigwager.model.SeoMeta;
import com.gigwager.model.SearchResultPattern;
import com.gigwager.model.WorkLevel;
import com.gigwager.model.CityLocalData;
import com.gigwager.model.content.CityRichContent;
import com.gigwager.model.content.WorkLevelRichContent;
import com.gigwager.util.AppConstants;
import com.gigwager.service.CityRichContentRepository;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.DriverShiftReportService;
import com.gigwager.service.HtmlSanitizerService;
import com.gigwager.service.PageEvidenceService;
import com.gigwager.service.PageIndexPolicyService;
import com.gigwager.dto.CityRankingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ProgrammaticSeoController {

        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        private final DataLayerService dataLayerService;
        private final PageIndexPolicyService pageIndexPolicyService;
        private final CityRichContentRepository cityRichContentRepository;
        private final HtmlSanitizerService htmlSanitizerService;
        private final DriverShiftReportService driverShiftReportService;
        private final PageEvidenceService pageEvidenceService;

        public ProgrammaticSeoController(DataLayerService dataLayerService,
                        PageIndexPolicyService pageIndexPolicyService,
                        CityRichContentRepository cityRichContentRepository,
                        HtmlSanitizerService htmlSanitizerService) {
                DriverShiftReportService driverShiftReportService = new DriverShiftReportService();
                this.dataLayerService = dataLayerService;
                this.pageIndexPolicyService = pageIndexPolicyService;
                this.cityRichContentRepository = cityRichContentRepository;
                this.htmlSanitizerService = htmlSanitizerService;
                this.driverShiftReportService = driverShiftReportService;
                this.pageEvidenceService = new PageEvidenceService(cityRichContentRepository, driverShiftReportService);
        }

        @Autowired
        public ProgrammaticSeoController(DataLayerService dataLayerService,
                        PageIndexPolicyService pageIndexPolicyService,
                        CityRichContentRepository cityRichContentRepository,
                        HtmlSanitizerService htmlSanitizerService,
                        DriverShiftReportService driverShiftReportService,
                        PageEvidenceService pageEvidenceService) {
                this.dataLayerService = dataLayerService;
                this.pageIndexPolicyService = pageIndexPolicyService;
                this.cityRichContentRepository = cityRichContentRepository;
                this.htmlSanitizerService = htmlSanitizerService;
                this.driverShiftReportService = driverShiftReportService;
                this.pageEvidenceService = pageEvidenceService;
        }

        @GetMapping("/salary/{app}")
        public String appHubPage(@PathVariable("app") String app, Model model) {
                if (!app.equals("uber") && !app.equals("doordash")) {
                        throw new com.gigwager.exception.ResourceNotFoundException("App not found");
                }
                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                List<CityRankingDto> topCities = Arrays.stream(CityData.values())
                                .filter(city -> pageIndexPolicyService.isCityReportIndexable(city, app))
                                .map(city -> {
                                        CityScenario scenario = calculateScenario("Side-Hustle",
                                                        city.getMarketTier().getSideHustleGross(), 250, 25, city, app);
                                        return new CityRankingDto(city, scenario.getNetHourly(), "Side-Hustle", scenario);
                                })
                                .filter(dto -> dto.netHourly() >= 6.0 && dto.netHourly() <= 45.0) // Sanity Gate
                                .sorted((c1, c2) -> Double.compare(c2.netHourly(), c1.netHourly()))
                                .limit(10)
                                .collect(Collectors.toList());

                if (topCities.isEmpty()) {
                        throw new com.gigwager.exception.ResourceNotFoundException("App pay data not available");
                }

                CityRankingDto topCity = topCities.get(0);
                long indexedCityCount = Arrays.stream(CityData.values())
                                .filter(city -> pageIndexPolicyService.isCityReportIndexable(city, app))
                                .count();

                // Dynamic Date
                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                CityRankingDto comparisonCity = topCities.stream()
                                .filter(dto -> pageIndexPolicyService.isCityReportIndexable(dto.city(), "uber"))
                                .filter(dto -> pageIndexPolicyService.isCityReportIndexable(dto.city(), "doordash"))
                                .findFirst()
                                .orElse(null);

                String coveragePath = app.equals("uber") ? "/uber/where-you-can-drive" : "/doordash/where-you-can-dash";

                String title = String.format("%s Driver Earnings by City: Hourly Pay After Expenses", appName);
                String description = String.format(
                                "Compare estimated %s driver hourly earnings across %d cities after mileage and self-employment tax. %s currently leads at about $%.2f/hr net. Updated %s.",
                                appName,
                                indexedCityCount,
                                topCity.city().getCityName(),
                                topCity.netHourly(),
                                monthYear);
                String canonicalUrl = String.format("%s/salary/%s", AppConstants.BASE_URL, app);

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("topCities", topCities);
                model.addAttribute("topCity", topCity);
                model.addAttribute("indexedCityCount", indexedCityCount);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("bestCitiesUrl", String.format("/best-cities/%s", app));
                model.addAttribute("calculatorUrl", "/" + app);
                model.addAttribute("coverageUrl", coveragePath);
                model.addAttribute("directoryUrl", "/salary/directory");
                model.addAttribute("topCityReportUrl",
                                String.format("/salary/%s/%s", app, topCity.city().getSlug()));
                model.addAttribute("comparisonUrl",
                                comparisonCity != null
                                                ? String.format("/compare/%s/uber-vs-doordash",
                                                                comparisonCity.city().getSlug())
                                                : "");
                model.addAttribute("comparisonCityName",
                                comparisonCity != null ? comparisonCity.city().getCityName() : "");
                model.addAttribute("appHubSchemaJsonLd",
                                buildAppHubSchemaGraph(appName, app, topCities, indexedCityCount));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "salary/app-hub";
        }

        @GetMapping("/reports/uber-driver-hourly-earnings-2026")
        public String uberHourlyEarningsReport(Model model) {
                List<CityEarningsSnapshot> snapshots = buildUberHourlyEarningsSnapshots();
                if (snapshots.isEmpty()) {
                        throw new com.gigwager.exception.ResourceNotFoundException("Uber hourly earnings report not available");
                }

                CityEarningsSnapshot topSnapshot = snapshots.stream()
                                .max((left, right) -> Double.compare(
                                                left.scenario().getNetHourly(),
                                                right.scenario().getNetHourly()))
                                .orElse(snapshots.get(0));
                double averageNetHourly = snapshots.stream()
                                .mapToDouble(snapshot -> snapshot.scenario().getNetHourly())
                                .average()
                                .orElse(0);

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String canonicalUrl = AppConstants.BASE_URL + "/reports/uber-driver-hourly-earnings-2026";
                String title = "Uber Driver Hourly Earnings 2026: City Net Pay Report";
                String description = String.format(
                                "Compare Uber driver hourly earnings in Atlanta, Los Angeles, Austin, Chicago, Houston, and Orlando after mileage and tax assumptions. Updated %s.",
                                monthYear);

                model.addAttribute("snapshots", snapshots);
                model.addAttribute("topSnapshot", topSnapshot);
                model.addAttribute("averageNetHourly", averageNetHourly);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("reportJsonLd", buildUberHourlyReportJsonLd(snapshots, canonicalUrl));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "reports/uber-hourly-earnings-2026";
        }

        @GetMapping("/reports/doordash-driver-hourly-pay-2026")
        public String doordashHourlyPayReport(Model model) {
                List<CityEarningsSnapshot> snapshots = buildDoorDashHourlyPaySnapshots();
                if (snapshots.isEmpty()) {
                        throw new com.gigwager.exception.ResourceNotFoundException("DoorDash hourly pay report not available");
                }

                CityEarningsSnapshot topSnapshot = snapshots.stream()
                                .max((left, right) -> Double.compare(
                                                left.scenario().getNetHourly(),
                                                right.scenario().getNetHourly()))
                                .orElse(snapshots.get(0));
                double averageNetHourly = snapshots.stream()
                                .mapToDouble(snapshot -> snapshot.scenario().getNetHourly())
                                .average()
                                .orElse(0);

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String canonicalUrl = AppConstants.BASE_URL + "/reports/doordash-driver-hourly-pay-2026";
                String title = "DoorDash Driver Hourly Pay 2026: City Earnings Report";
                String description = String.format(
                                "Compare DoorDash driver hourly pay in New York, Denver, Phoenix, Indianapolis, San Jose, Dallas, Los Angeles, and Chicago after mileage and tax assumptions. Updated %s.",
                                monthYear);

                model.addAttribute("snapshots", snapshots);
                model.addAttribute("topSnapshot", topSnapshot);
                model.addAttribute("averageNetHourly", averageNetHourly);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForApp("doordash"));
                model.addAttribute("reportJsonLd", buildDoorDashHourlyReportJsonLd(snapshots, canonicalUrl));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "reports/doordash-hourly-pay-2026";
        }

        @GetMapping("/reports/doordash-driver-shift-evidence-2026")
        public String doordashDriverShiftEvidenceReport(Model model) {
                List<DriverShiftReport> reports = driverShiftReportService.getReportsForApp("doordash");
                if (reports.isEmpty()) {
                        throw new com.gigwager.exception.ResourceNotFoundException("DoorDash shift evidence not available");
                }

                long netCheckCount = reports.stream()
                                .filter(DriverShiftReport::hasEstimatedNetCheck)
                                .count();
                double averageReportedNetHourly = reports.stream()
                                .filter(DriverShiftReport::hasEstimatedNetCheck)
                                .mapToDouble(DriverShiftReport::estimatedNetHourlyAfterMileageAndTax)
                                .average()
                                .orElse(0);
                long citySpecificCount = reports.stream()
                                .filter(report -> !"benchmark".equals(report.citySlug()))
                                .count();

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String canonicalUrl = AppConstants.BASE_URL + "/reports/doordash-driver-shift-evidence-2026";
                String title = "DoorDash Driver Shift Evidence 2026: Gross, Miles, Active Time";
                String description = String.format(
                                "Reviewed DoorDash shift evidence for gross pay, miles, active time, dash time, and estimated net pay after mileage. Updated %s.",
                                monthYear);

                model.addAttribute("driverShiftReports", reports);
                model.addAttribute("reportCount", reports.size());
                model.addAttribute("citySpecificCount", citySpecificCount);
                model.addAttribute("netCheckCount", netCheckCount);
                model.addAttribute("averageReportedNetHourly", averageReportedNetHourly);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("evidenceJsonLd", buildDoorDashShiftEvidenceJsonLd(reports, canonicalUrl));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "reports/doordash-shift-evidence-2026";
        }

        @GetMapping("/doordash/adjustment-pay-calculator")
        public String doordashAdjustmentPayCalculator(Model model) {
                List<DoorDashAdjustmentScenario> scenarios = buildDoorDashAdjustmentScenarios();

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String canonicalUrl = AppConstants.BASE_URL + "/doordash/adjustment-pay-calculator";
                String title = "DoorDash Adjustment Pay Calculator: Prop 22, NYC, Seattle";
                String description = String.format(
                                "Estimate DoorDash adjustment pay for California Prop 22, NYC minimum earnings, and Seattle adjusted pay using active time, active miles, DoorDash pay before tips, and official formulas. Updated %s.",
                                monthYear);

                model.addAttribute("scenarios", scenarios);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("adjustmentJsonLd", buildDoorDashAdjustmentPayJsonLd(canonicalUrl));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "reports/doordash-adjustment-pay-calculator";
        }

        @GetMapping("/doordash/how-much-can-you-make-in-{durationSlug}")
        public String doordashDurationEarningsPage(@PathVariable("durationSlug") String durationSlug, Model model) {
                DoorDashDurationEstimate estimate = buildDoorDashDurationEstimates().stream()
                                .filter(item -> item.slug().equals(durationSlug))
                                .findFirst()
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "DoorDash duration estimate not found"));

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String canonicalUrl = AppConstants.BASE_URL + "/doordash/how-much-can-you-make-in-" + estimate.slug();
                String title = String.format("How Much Can You Make with DoorDash in %s? 2026 Net Pay",
                                estimate.displayName());
                String description = estimate.isWeekly()
                                ? String.format(
                                                "DoorDash weekly earnings estimate: $%d-$%d gross and about $%d-$%d net after mileage and tax reserves. Updated %s.",
                                                estimate.grossLow(),
                                                estimate.grossHigh(),
                                                estimate.netLow(),
                                                estimate.netHigh(),
                                                monthYear)
                                : String.format(
                                                "DoorDash %s earnings estimate: $%d-$%d gross and about $%d-$%d net after mileage and tax reserves. Updated %s.",
                                                estimate.displayName().toLowerCase(java.util.Locale.US),
                                                estimate.grossLow(),
                                                estimate.grossHigh(),
                                                estimate.netLow(),
                                                estimate.netHigh(),
                                                monthYear);

                model.addAttribute("estimate", estimate);
                model.addAttribute("allEstimates", buildDoorDashDurationEstimates());
                model.addAttribute("moneyIntents", buildDoorDashMoneyIntents());
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForApp("doordash"));
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("durationJsonLd", buildDoorDashDurationJsonLd(estimate, canonicalUrl));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "reports/doordash-duration-earnings";
        }

        @GetMapping("/doordash/{moneySlug:can-you-make-100-a-day|can-you-make-200-a-day|after-gas|pay-per-mile}")
        public String doordashMoneyIntentPage(@PathVariable("moneySlug") String moneySlug, Model model) {
                DoorDashMoneyIntent intent = buildDoorDashMoneyIntents().stream()
                                .filter(item -> item.slug().equals(moneySlug))
                                .findFirst()
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "DoorDash money intent not found"));

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String canonicalUrl = AppConstants.BASE_URL + intent.path();
                String title = buildDoorDashMoneyIntentTitle(intent);
                String description = String.format(
                                "%s: %s Typical gross %s, net %s, %s, and %s. Updated %s.",
                                intent.searchPhrase(),
                                intent.answerLead(),
                                intent.grossRange(),
                                intent.netRange(),
                                intent.hourRange(),
                                intent.mileRange(),
                                monthYear);

                model.addAttribute("intent", intent);
                model.addAttribute("moneyIntents", buildDoorDashMoneyIntents());
                model.addAttribute("durationEstimates", buildDoorDashDurationEstimates());
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForApp("doordash"));
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("moneyIntentJsonLd", buildDoorDashMoneyIntentJsonLd(intent, canonicalUrl));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "reports/doordash-money-intent";
        }

        @GetMapping({
                        "/doordash/100-dollars-a-day",
                        "/doordash/make-100-a-day",
                        "/doordash/make-100-dollars-a-day",
                        "/doordash/can-doordash-make-100-a-day",
                        "/doordash/how-to-make-100-a-day"
        })
        public RedirectView redirectDoorDashHundredDayAliases() {
                return permanentRedirect("/doordash/can-you-make-100-a-day");
        }

        @GetMapping({
                        "/doordash/200-dollars-a-day",
                        "/doordash/make-200-a-day",
                        "/doordash/make-200-dollars-a-day",
                        "/doordash/can-doordash-make-200-a-day",
                        "/doordash/how-to-make-200-a-day"
        })
        public RedirectView redirectDoorDashTwoHundredDayAliases() {
                return permanentRedirect("/doordash/can-you-make-200-a-day");
        }

        @GetMapping({
                        "/doordash/does-doordash-pay-for-gas",
                        "/doordash/how-much-does-doordash-pay-after-gas",
                        "/doordash/how-much-do-you-make-after-gas",
                        "/doordash/doordash-after-gas",
                        "/doordash/does-doordash-pay-for-mileage"
        })
        public RedirectView redirectDoorDashAfterGasAliases() {
                return permanentRedirect("/doordash/after-gas");
        }

        @GetMapping({
                        "/doordash/how-much-does-doordash-pay-per-mile",
                        "/doordash/dollars-per-mile",
                        "/doordash/dollar-per-mile",
                        "/doordash/mileage-pay",
                        "/doordash/best-orders-per-mile"
        })
        public RedirectView redirectDoorDashPayPerMileAliases() {
                return permanentRedirect("/doordash/pay-per-mile");
        }

        @GetMapping({
                        "/doordash/how-much-can-you-make-in-one-day",
                        "/doordash/how-much-can-you-make-in-1-day",
                        "/doordash/how-much-can-you-make-in-a-full-day",
                        "/doordash/how-much-can-you-make-in-10-hours",
                        "/doordash/best-time-to-doordash"
        })
        public RedirectView redirectDoorDashDayAndTimingAliases() {
                return permanentRedirect("/doordash/how-much-can-you-make-in-a-day");
        }

        @GetMapping({
                        "/doordash/adjustment-pay",
                        "/doordash/prop-22-calculator",
                        "/doordash/prop-22-pay-calculator",
                        "/doordash/nyc-adjustment-pay",
                        "/doordash/seattle-adjusted-pay"
        })
        public RedirectView redirectDoorDashAdjustmentPayAliases() {
                return permanentRedirect("/doordash/adjustment-pay-calculator");
        }

        @GetMapping({
                        "/uber/after-gas",
                        "/uber/driver-after-expenses",
                        "/uber/how-much-do-drivers-make-after-gas",
                        "/uber/does-uber-pay-for-gas"
        })
        public RedirectView redirectUberAfterExpenseAliases() {
                return permanentRedirect("/uber-after-expenses");
        }

        @GetMapping({
                        "/uber/100-dollars-a-day",
                        "/uber/make-100-a-day",
                        "/uber/can-you-make-100-a-day",
                        "/uber/how-to-make-100-a-day"
        })
        public RedirectView redirectUberHundredDayAliases() {
                return permanentRedirect("/salary/uber/chicago/100-a-day");
        }

        @GetMapping({
                        "/uber/pay-per-mile",
                        "/uber/dollars-per-mile",
                        "/uber/mileage-pay"
        })
        public RedirectView redirectUberPayPerMileAliases() {
                return permanentRedirect("/salary/uber/chicago/per-mile");
        }

        @GetMapping("/uber/where-you-can-drive")
        public String uberCoveragePage(Model model) {
                return renderCoveragePage(
                                "uber",
                                "Uber",
                                "Drive",
                                "/uber/where-you-can-drive",
                                "Uber's official city directory",
                                "Use Uber's own city directory to confirm whether your target city or metro is active before comparing take-home pay.",
                                "https://www.uber.com/us/en/e/drive/cities/",
                                "Open Uber's official city directory",
                                "Use Uber's official driver city directory to confirm current market availability because onboarding and product coverage can change by city and metro area.",
                                model);
        }

        @GetMapping("/doordash/where-you-can-dash")
        public String doordashCoveragePage(Model model) {
                return renderCoveragePage(
                                "doordash",
                                "DoorDash",
                                "Dash",
                                "/doordash/where-you-can-dash",
                                "DoorDash Dasher signup and availability flow",
                                "Use DoorDash's Dasher signup flow to confirm whether your area is open for onboarding and whether you can dash there right now.",
                                "https://dasher.doordash.com/en-us",
                                "Open DoorDash's Dasher signup flow",
                                "Use DoorDash's Dasher signup and local availability flow to confirm whether your area is open for onboarding or active dashing, because market access can change over time.",
                                model);
        }

        @GetMapping("/best-cities/{app}")
        public String bestCitiesPage(@PathVariable("app") String app, Model model) {
                if (!app.equals("uber") && !app.equals("doordash")) {
                        throw new com.gigwager.exception.ResourceNotFoundException("App not found");
                }
                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                List<CityRankingDto> rankedCities = Arrays.stream(CityData.values())
                                .filter(city -> pageIndexPolicyService.isCityReportIndexable(city, app))
                                .map(city -> {
                                        CityScenario scenario = calculateScenario("Side-Hustle",
                                                        city.getMarketTier().getSideHustleGross(), 250, 25, city, app);
                                        return new CityRankingDto(city, scenario.getNetHourly(), "Side-Hustle", scenario);
                                })
                                .filter(dto -> dto.netHourly() >= 6.0 && dto.netHourly() <= 45.0) // Sanity Gate
                                .sorted((c1, c2) -> Double.compare(c2.netHourly(), c1.netHourly()))
                                .collect(Collectors.toList());

                if (rankedCities.isEmpty()) {
                        throw new com.gigwager.exception.ResourceNotFoundException("City ranking not available");
                }

                CityRankingDto topRankedCity = rankedCities.get(0);

                java.time.LocalDate now = java.time.LocalDate.now();
                int currentYear = now.getYear();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                String title;
                String description;
                if (app.equals("doordash")) {
                        title = String.format("Best Places to DoorDash %d: %d Cities Ranked by Net Pay",
                                        currentYear, rankedCities.size());
                        description = String.format(
                                        "Find the best places to DoorDash in %d, ranked by estimated net hourly pay after gas, mileage, and tax assumptions. Start with %s at about $%.2f/hr net, then compare %d city markets and hours to $100.",
                                        currentYear,
                                        topRankedCity.city().getCityName(),
                                        topRankedCity.netHourly(),
                                        rankedCities.size());
                } else {
                        title = String.format("Best Cities to Uber in %d: Driver Net Pay by Market",
                                        currentYear);
                        description = String.format(
                                        "Compare the best cities to Uber in %d using estimated net hourly pay after mileage and self-employment tax. %s leads at about $%.2f/hr; open each market's calculator before you drive.",
                                        currentYear,
                                        topRankedCity.city().getCityName(),
                                        topRankedCity.netHourly());
                }
                String canonicalUrl = String.format("%s/best-cities/%s", AppConstants.BASE_URL, app);
                String coverageUrl = app.equals("uber") ? "/uber/where-you-can-drive" : "/doordash/where-you-can-dash";
                String coverageGuideTitle = app.equals("uber") ? "Uber coverage guide" : "DoorDash availability guide";
                String coverageGuideDescription = app.equals("uber")
                                ? "Verify whether Uber is active in your city first, then come back here when you want the highest-paying markets."
                                : "Check DoorDash onboarding and local availability first, then come back here when you want the highest-paying markets.";
                CityRankingDto runnerUpCity = rankedCities.size() > 1 ? rankedCities.get(1) : topRankedCity;
                double leadOverRunnerUp = Math.max(0, topRankedCity.netHourly() - runnerUpCity.netHourly());

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("rankedCities", rankedCities);
                model.addAttribute("topRankedCity", topRankedCity);
                model.addAttribute("runnerUpCity", runnerUpCity);
                model.addAttribute("leadOverRunnerUp", leadOverRunnerUp);
                model.addAttribute("rankedCityCount", rankedCities.size());
                model.addAttribute("topThreeCities", formatTopCityList(rankedCities, 3));
                model.addAttribute("currentYear", currentYear);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("appHubUrl", String.format("/salary/%s", app));
                model.addAttribute("topCityReportUrl", String.format("/salary/%s/%s", app, topRankedCity.city().getSlug()));
                model.addAttribute("coverageUrl", coverageUrl);
                model.addAttribute("coverageGuideTitle", coverageGuideTitle);
                model.addAttribute("coverageGuideDescription", coverageGuideDescription);
                model.addAttribute("methodologyUrl", "/methodology");
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForApp(app));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));
                model.addAttribute("dataLayerService", dataLayerService);
                model.addAttribute("itemListJsonLd", buildBestCitiesItemListJsonLd(appName, app, rankedCities));
                model.addAttribute("faqJsonLd", buildBestCitiesFaqJsonLd(appName, app, rankedCities, currentYear));

                return "salary/best-cities";
        }

        @GetMapping("/compare/{citySlug}/uber-vs-doordash")
        public String comparePage(@PathVariable("citySlug") String citySlug, Model model) {
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "City not found"));

                // Only generate comparisons where both app city reports pass the same
                // evidence-backed indexing policy.
                if (!pageIndexPolicyService.isCityReportIndexable(city, "uber")
                                || !pageIndexPolicyService.isCityReportIndexable(city, "doordash")) {
                        throw new com.gigwager.exception.ResourceNotFoundException(
                                        "Detailed comparison not available for this city yet");
                }

                CityScenario uberScenario = calculateScenario("Side-Hustle", city.getMarketTier().getSideHustleGross(),
                                250, 25, city, "uber");
                CityScenario doordashScenario = calculateScenario("Side-Hustle",
                                city.getMarketTier().getSideHustleGross(), 250, 25, city, "doordash");

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                double netHourlyGap = Math.abs(uberScenario.getNetHourly() - doordashScenario.getNetHourly());
                boolean nearlyTied = netHourlyGap < 0.25;
                boolean uberWins = uberScenario.getNetHourly() >= doordashScenario.getNetHourly();
                String winningAppName = uberWins ? "Uber" : "DoorDash";
                String losingAppName = uberWins ? "DoorDash" : "Uber";
                double winningNetHourly = uberWins ? uberScenario.getNetHourly() : doordashScenario.getNetHourly();

                String title;
                String description;
                if (nearlyTied) {
                        title = String.format("Uber Eats vs DoorDash %s: Same Net Pay After Gas",
                                        city.getCityName());
                        description = String.format(
                                        "Uber Eats vs DoorDash in %s are effectively tied at about $%.2f/hr net after mileage, gas, and tax assumptions. Compare hours, miles, and city evidence. Updated %s.",
                                        city.getCityName(),
                                        winningNetHourly,
                                        monthYear);
                } else {
                        title = String.format("Uber Eats vs DoorDash %s: %s by $%.2f/hr Net",
                                        city.getCityName(),
                                        winningAppName,
                                        netHourlyGap);
                        description = String.format(
                                        "Uber Eats vs DoorDash in %s: %s leads at about $%.2f/hr net, $%.2f/hr ahead of %s after mileage, gas, and tax assumptions. Updated %s.",
                                        city.getCityName(),
                                        winningAppName,
                                        winningNetHourly,
                                        netHourlyGap,
                                        losingAppName,
                                        monthYear);
                }
                String canonicalUrl = String.format("%s/compare/%s/uber-vs-doordash", AppConstants.BASE_URL, citySlug);
                PageEvidenceProfile pageEvidenceProfile = pageEvidenceService.comparisonReport(
                                city,
                                uberScenario,
                                doordashScenario,
                                winningAppName,
                                netHourlyGap,
                                true);

                model.addAttribute("city", city);
                model.addAttribute("uberScenario", uberScenario);
                model.addAttribute("doordashScenario", doordashScenario);
                model.addAttribute("winningAppName", winningAppName);
                model.addAttribute("losingAppName", losingAppName);
                model.addAttribute("winningNetHourly", winningNetHourly);
                model.addAttribute("netHourlyGap", netHourlyGap);
                model.addAttribute("nearlyTied", nearlyTied);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("localData", dataLayerService.getLocalData(citySlug));
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForCity("doordash", citySlug));
                model.addAttribute("pageEvidenceProfile", pageEvidenceProfile);
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "salary/compare";
        }

        @GetMapping("/salary/{app}/{citySlug}")
        public String citySalaryPage(@PathVariable("app") String app,
                        @PathVariable("citySlug") String citySlug,
                        Model model) {

                // Validate app
                if (!app.equals("uber") && !app.equals("doordash")) {
                        throw new com.gigwager.exception.ResourceNotFoundException("App not found");
                }

                // Resolve city from slug
                // FIX: Enforce Hard 404s for invalid cities
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "City not found"));

                // Generate 3 scenarios based on MarketTier
                List<CityScenario> scenarios = generateScenarios(city, app);

                // Select "Featured" scenario (side-hustle level)
                CityScenario featuredScenario = scenarios.get(1);

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                // Build unique SEO meta
                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                String title = String.format("%s Driver Earnings in %s: $%.2f/hr Net Hourly %d",
                                appName, city.getCityName(),
                                featuredScenario.getNetHourly(), now.getYear());
                String description = String.format(
                                "Estimated %s driver hourly earnings in %s for %d: $%.2f/hr net after mileage and SE tax on a 25-hour baseline. Updated %s.",
                                appName, city.getCityName(), now.getYear(),
                                featuredScenario.getNetHourly(), monthYear);
                String heroTitlePrimary = String.format("%s Driver Earnings in %s", appName,
                                city.getCityName());
                String heroTitleSecondary = String.format("$%.2f/hr Net Hourly After Expenses",
                                featuredScenario.getNetHourly());
                String heroTitleTertiary = String.format("%d Rideshare Driver Hourly Earnings", now.getYear());
                String heroSummary = String.format(
                                "Estimated %s driver hourly earnings in %s start at $%.2f/hr net in our %d side-hustle model. The baseline uses $%d/week gross, %d mi, %d hrs, and $%.2f/gal gas; use the calculator below to adjust your own numbers.",
                                appName,
                                city.getCityName(),
                                featuredScenario.getNetHourly(),
                                now.getYear(),
                                featuredScenario.getGrossWeekly(),
                                featuredScenario.getMiles(),
                                featuredScenario.getHours(),
                                city.getGasPrice());

                String canonicalUrl = String.format("%s/salary/%s/%s", AppConstants.BASE_URL, app, citySlug);
                String appHubCanonicalUrl = String.format("%s/salary/%s", AppConstants.BASE_URL, app);
                String calculatorUrl = buildCalculatorUrl(app, featuredScenario, city);

                // Cross-App Silo: Generate link to the other app
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";
                String otherAppUrl = String.format("/salary/%s/%s", otherApp, citySlug);
                boolean cityIndexable = pageIndexPolicyService.isCityReportIndexable(city, app)
                                && featuredScenario.getNetHourly() >= 6.0
                                && featuredScenario.getNetHourly() <= 45.0;
                PageEvidenceProfile pageEvidenceProfile = pageEvidenceService.cityReport(
                                app,
                                appName,
                                city,
                                featuredScenario,
                                cityIndexable);
                CityRichContent cityReportRichContent = cityRichContentRepository.findBySlug(citySlug).orElse(null);
                WorkLevelRichContent cityReportSideHustleContent = cityReportRichContent != null
                                && cityReportRichContent.workLevels() != null
                                                ? cityReportRichContent.workLevels().get("side-hustle")
                                                : null;

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("city", city);
                model.addAttribute("scenarios", scenarios);
                model.addAttribute("featuredScenario", featuredScenario);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("otherApp", otherApp);
                model.addAttribute("otherAppName", otherAppName);
                model.addAttribute("otherAppUrl", otherAppUrl);
                model.addAttribute("heroTitlePrimary", heroTitlePrimary);
                model.addAttribute("heroTitleSecondary", heroTitleSecondary);
                model.addAttribute("heroTitleTertiary", heroTitleTertiary);
                model.addAttribute("heroSummary", heroSummary);
                model.addAttribute("driverFieldNotes", buildDriverFieldNotes(app, appName, city, featuredScenario));
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForCity(app, citySlug));
                model.addAttribute("searchResultPatterns",
                                buildSearchResultPatterns(app, appName, city, null, featuredScenario));
                model.addAttribute("methodologyUrl", "/methodology");
                model.addAttribute("calculatorUrl", calculatorUrl);
                model.addAttribute("taxEstimatorUrl", buildTaxEstimatorUrl(app, featuredScenario));
                model.addAttribute("bestCitiesUrl", String.format("/best-cities/%s", app));
                model.addAttribute("compareUrl",
                                pageIndexPolicyService.isCityReportIndexable(city, "uber")
                                                && pageIndexPolicyService.isCityReportIndexable(city, "doordash")
                                                ? String.format("/compare/%s/uber-vs-doordash", citySlug)
                                                : "");
                model.addAttribute("safeMarketDescription", htmlSanitizerService.sanitize(city.getMarketDescription()));
                model.addAttribute("cityFaqJsonLd", buildCityFaqJsonLd(appName, city, featuredScenario));
                model.addAttribute("pageEvidenceProfile", pageEvidenceProfile);
                model.addAttribute("localRichContent", cityReportRichContent);
                model.addAttribute("localSideHustleContent", cityReportSideHustleContent);

                if (!cityIndexable) {
                        model.addAttribute("noIndex", true);
                        canonicalUrl = appHubCanonicalUrl;
                }
                model.addAttribute("cityCalculatorJsonLd", buildCityCalculatorJsonLd(
                                appName,
                                city,
                                featuredScenario,
                                canonicalUrl,
                                calculatorUrl));

                // Pass raw description to template if needed, or rely on SeoMeta
                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                AppConstants.BASE_URL + "/og-image.jpg"));

                // Internal Linking Silo: 3 random cities with same MarketTier
                List<CityData> similarCities = Arrays.stream(CityData.values())
                                .filter(c -> c.getMarketTier() == city.getMarketTier()) // Same Economy Tier
                                .filter(c -> pageIndexPolicyService.isCityReportIndexable(c, app))
                                .filter(c -> !c.equals(city)) // Exclude current city
                                .sorted((c1, c2) -> {
                                        // Deterministic sorting based on hash of slugs to stabilize internal linking
                                        String hash1 = c1.getSlug() + city.getSlug();
                                        String hash2 = c2.getSlug() + city.getSlug();
                                        return Integer.compare(hash1.hashCode(), hash2.hashCode());
                                })
                                .limit(3)
                                .collect(Collectors.toList());
                model.addAttribute("similarCities", similarCities);

                return "salary/city-report";
        }

        /**
         * Work-Level Deep Dive Pages
         * Separate URLs for part-time, side-hustle, full-time scenarios
         */
        @GetMapping("/salary/{app}/{citySlug}/{workLevelSlug}")
        public String workLevelDeepDive(@PathVariable("app") String app,
                        @PathVariable("citySlug") String citySlug,
                        @PathVariable("workLevelSlug") String workLevelSlug,
                        Model model) {

                // Validate app
                if (!app.equals("uber") && !app.equals("doordash")) {
                        throw new com.gigwager.exception.ResourceNotFoundException("App not found");
                }

                // Resolve city from slug
                // FIX: Enforce Hard 404s for invalid cities
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "City not found"));

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                java.util.Optional<CityIntentPage> intentPage = CityIntentPage.fromSlug(workLevelSlug);
                if (intentPage.isPresent()) {
                        if (!intentPage.get().isSupportedForApp(app)) {
                                throw new com.gigwager.exception.ResourceNotFoundException("Intent page not found");
                        }
                        return cityIntentPage(app, appName, city, intentPage.get(), monthYear, model);
                }

                // Resolve work level from slug
                WorkLevel workLevel;
                try {
                        workLevel = WorkLevel.fromSlug(workLevelSlug);
                } catch (IllegalArgumentException e) {
                        throw new com.gigwager.exception.ResourceNotFoundException("Work level not found");
                }

                // Generate scenario for this specific work level
                CityScenario scenario = generateScenarioByWorkLevel(city, app, workLevel);

                // Build unique SEO meta
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";

                String title = String.format("%s Driver Earnings in %s %s: $%.2f/hr Net",
                                appName, city.getCityName(), workLevel.getDisplayName(),
                                scenario.getNetHourly());
                String description = String.format(
                                "Estimated %s %s earnings in %s: $%.2f/hr net after mileage and self-employment tax, with a prefilled calculator for your own miles and hours.",
                                appName, workLevel.getDisplayName(), city.getCityName(),
                                scenario.getNetHourly());

                String canonicalUrl = String.format("%s/salary/%s/%s/%s", AppConstants.BASE_URL, app, citySlug,
                                workLevelSlug);

                // Cross-App Silo
                String otherAppUrl = String.format("/salary/%s/%s/%s", otherApp, citySlug, workLevelSlug);
                String calculatorUrl = buildCalculatorUrl(app, scenario, city);

                // Parent page (main city calculator) for breadcrumb
                String parentPageUrl = String.format("/salary/%s/%s", app, citySlug);
                String parentCanonicalUrl = String.format("%s%s", AppConstants.BASE_URL, parentPageUrl);

                // Freshness signal
                String lastUpdated = monthYear;

                // Fetch CityLocalData to replace placeholder tokens
                CityLocalData localData = dataLayerService.getLocalData(city.getSlug());

                // Legacy template fallback blocks (kept for non-pilot cities)
                String workLevelMeaning = workLevel.getWorkLevelMeaning(appName, city.getCityName(), localData);
                String taxStrategy = workLevel.getTaxStrategy(appName, city.getCityName(), localData);
                String dayInTheLife = workLevel.getDayInTheLife(appName, city.getCityName(), city, localData);
                String bestPractices = workLevel.getBestPractices(appName, city.getCityName(), city, localData);
                String pageStructureType = "TYPE_A";
                String heroHook = null;
                String methodologyVersion = "legacy-v1";
                String contentType = "legacy_template";
                String lastVerifiedAt = null;
                List<com.gigwager.model.content.PersonaQuote> personaQuotes = Collections.emptyList();
                List<com.gigwager.model.content.SourceCitation> sourceCitations = Collections.emptyList();
                Double richNetMin = null;
                Double richNetMax = null;

                // Rich city content (pilot cities): replaces Mad-Libs blocks with pre-generated
                // structured content.
                CityRichContent richCity = cityRichContentRepository.findBySlug(citySlug).orElse(null);
                if (richCity != null) {
                        if (richCity.seo() != null) {
                                pageStructureType = richCity.seo().pageStructureType().name();
                                heroHook = richCity.seo().heroHook();
                                methodologyVersion = richCity.seo().methodologyVersion();
                                contentType = richCity.seo().contentType();
                                lastVerifiedAt = richCity.seo().lastVerifiedAt();
                                if (richCity.seo().sources() != null) {
                                        sourceCitations = richCity.seo().sources();
                                }
                        }

                        if (richCity.workLevels() != null) {
                                WorkLevelRichContent richWorkLevel = richCity.workLevels().get(workLevelSlug);
                                if (richWorkLevel != null) {
                                        workLevelMeaning = chooseNonBlank(richWorkLevel.workLevelMeaningHtml(), workLevelMeaning);
                                        taxStrategy = chooseNonBlank(richWorkLevel.taxStrategyHtml(), taxStrategy);
                                        dayInTheLife = chooseNonBlank(richWorkLevel.dayInTheLifeHtml(), dayInTheLife);
                                        bestPractices = chooseNonBlank(richWorkLevel.bestPracticesHtml(), bestPractices);
                                        heroHook = chooseNonBlank(richWorkLevel.localStrategyText(), heroHook);
                                        if (richWorkLevel.personaQuotes() != null) {
                                                personaQuotes = richWorkLevel.personaQuotes();
                                        }
                                        if (richWorkLevel.realisticNetHourlyRange() != null) {
                                                richNetMin = richWorkLevel.realisticNetHourlyRange().min();
                                                richNetMax = richWorkLevel.realisticNetHourlyRange().max();
                                        }
                                }
                        }
                }

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("city", city);
                model.addAttribute("workLevel", workLevel);
                model.addAttribute("scenario", scenario);
                model.addAttribute("lastUpdated", lastUpdated);
                model.addAttribute("otherApp", otherApp);
                model.addAttribute("otherAppName", otherAppName);
                model.addAttribute("otherAppUrl", otherAppUrl);
                model.addAttribute("parentPageUrl", parentPageUrl);
                model.addAttribute("calculatorUrl", calculatorUrl);
                model.addAttribute("driverFieldNotes", buildDriverFieldNotes(app, appName, city, scenario));
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForCity(app, citySlug));

                boolean workLevelIndexable = pageIndexPolicyService.isWorkLevelReportIndexable(city, workLevel, app)
                                && scenario.getNetHourly() >= 6.0
                                && scenario.getNetHourly() <= 45.0;
                PageEvidenceProfile pageEvidenceProfile = pageEvidenceService.workLevelReport(
                                app,
                                appName,
                                city,
                                workLevel,
                                scenario,
                                workLevelIndexable);
                model.addAttribute("pageEvidenceProfile", pageEvidenceProfile);
                if (!workLevelIndexable) {
                        model.addAttribute("noIndex", true);
                        canonicalUrl = parentCanonicalUrl;
                }

                // Unique content sections
                model.addAttribute("workLevelMeaning", htmlSanitizerService.sanitize(workLevelMeaning));
                model.addAttribute("taxStrategy", htmlSanitizerService.sanitize(taxStrategy));
                model.addAttribute("dayInTheLife", htmlSanitizerService.sanitize(dayInTheLife));
                model.addAttribute("bestPractices", htmlSanitizerService.sanitize(bestPractices));
                model.addAttribute("pageStructureType", pageStructureType);
                model.addAttribute("heroHook", heroHook);
                model.addAttribute("personaQuotes", personaQuotes);
                model.addAttribute("sourceCitations", sourceCitations);
                model.addAttribute("methodologyVersion", methodologyVersion);
                model.addAttribute("contentType", contentType);
                model.addAttribute("requiresEditorialReview", requiresEditorialReview(contentType));
                model.addAttribute("editorialReviewLabel", editorialReviewLabel(contentType));
                model.addAttribute("lastVerifiedAt", lastVerifiedAt);
                model.addAttribute("richNetMin", richNetMin);
                model.addAttribute("richNetMax", richNetMax);

                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                AppConstants.BASE_URL + "/og-image.jpg"));
                model.addAttribute("workLevelJsonLd", buildWorkLevelJsonLd(
                                city,
                                appName,
                                workLevel,
                                scenario,
                                otherAppName,
                                parentPageUrl,
                                canonicalUrl,
                                calculatorUrl));

                // Internal Linking Silo: 3 random cities with same MarketTier
                List<CityData> similarCities = Arrays.stream(CityData.values())
                                .filter(c -> c.getMarketTier() == city.getMarketTier()) // Same Economy Tier
                                .filter(c -> pageIndexPolicyService.isWorkLevelReportIndexable(c, workLevel, app))
                                .filter(c -> !c.equals(city)) // Exclude current city
                                .sorted((c1, c2) -> {
                                        // Deterministic sorting based on hash of slugs to stabilize internal linking
                                        String hash1 = c1.getSlug() + city.getSlug();
                                        String hash2 = c2.getSlug() + city.getSlug();
                                        return Integer.compare(hash1.hashCode(), hash2.hashCode());
                                })
                                .limit(3)
                                .collect(Collectors.toList());
                model.addAttribute("similarCities", similarCities);

                return "salary/city-work-level";
        }

        private String cityIntentPage(String app,
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        String monthYear,
                        Model model) {
                CityScenario scenario = generateScenarioByWorkLevel(city, app, WorkLevel.SIDE_HUSTLE);
                String canonicalUrl = String.format("%s/salary/%s/%s/%s",
                                AppConstants.BASE_URL,
                                app,
                                city.getSlug(),
                                intentPage.getSlug());
                String parentPageUrl = String.format("/salary/%s/%s", app, city.getSlug());
                String title = buildCityIntentTitle(appName, city, intentPage, scenario);
                String description = buildCityIntentDescription(appName, city, intentPage, scenario, monthYear);
                String answerHtml = buildCityIntentAnswerHtml(appName, city, intentPage, scenario);
                CityRichContent localRichContent = cityRichContentRepository.findBySlug(city.getSlug()).orElse(null);
                WorkLevelRichContent localSideHustleContent = localRichContent != null
                                && localRichContent.workLevels() != null
                                                ? localRichContent.workLevels().get("side-hustle")
                                                : null;

                boolean indexable = pageIndexPolicyService.isCityIntentPageIndexable(city, app, intentPage)
                                && scenario.getNetHourly() >= 6.0
                                && scenario.getNetHourly() <= 45.0;
                PageEvidenceProfile pageEvidenceProfile = pageEvidenceService.intentReport(
                                app,
                                appName,
                                city,
                                intentPage,
                                scenario,
                                indexable);
                if (!indexable) {
                        model.addAttribute("noIndex", true);
                        canonicalUrl = String.format("%s/salary/%s", AppConstants.BASE_URL, app);
                }

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("city", city);
                model.addAttribute("intentPage", intentPage);
                model.addAttribute("scenario", scenario);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("parentPageUrl", parentPageUrl);
                model.addAttribute("calculatorUrl", buildCalculatorUrl(app, scenario, city));
                model.addAttribute("bestCitiesUrl", String.format("/best-cities/%s", app));
                model.addAttribute("driverFieldNotes", buildDriverFieldNotes(app, appName, city, scenario));
                model.addAttribute("driverShiftReports", driverShiftReportService.getReportsForCity(app, city.getSlug()));
                model.addAttribute("pageEvidenceProfile", pageEvidenceProfile);
                model.addAttribute("intentMetrics", buildCityIntentMetrics(city, intentPage, scenario));
                model.addAttribute("intentEvidencePatterns",
                                buildCityIntentEvidencePatterns(app, appName, city, intentPage, scenario));
                model.addAttribute("searchResultPatterns",
                                buildSearchResultPatterns(app, appName, city, intentPage, scenario));
                model.addAttribute("daily100Hours", hoursToNetTarget(scenario, 100));
                model.addAttribute("daily100Miles", milesForHours(scenario, hoursToNetTarget(scenario, 100)));
                model.addAttribute("daily100Gross", grossForHours(scenario, hoursToNetTarget(scenario, 100)));
                model.addAttribute("daily200Hours", hoursToNetTarget(scenario, 200));
                model.addAttribute("daily200Miles", milesForHours(scenario, hoursToNetTarget(scenario, 200)));
                model.addAttribute("daily200Gross", grossForHours(scenario, hoursToNetTarget(scenario, 200)));
                model.addAttribute("answerHtml", answerHtml);
                model.addAttribute("localRichContent", localRichContent);
                model.addAttribute("localSideHustleContent", localSideHustleContent);
                model.addAttribute("cityIntentJsonLd",
                                buildCityIntentJsonLd(appName, city, intentPage, scenario, canonicalUrl));
                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                AppConstants.BASE_URL + "/og-image.jpg"));

                return "salary/city-intent";
        }

        private String buildCityIntentTitle(
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario) {
                return switch (intentPage) {
                        case AFTER_GAS -> String.format("%s %s After Gas: $%.2f/hr Net",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly());
                        case PER_MILE -> String.format("%s %s Pay Per Mile: $%.2f/hr Net",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly());
                        case ACTIVE_TIME -> String.format("%s %s Active Time Pay: $%.2f/hr Net",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly());
                        case WORTH_IT -> String.format("Is %s Worth It in %s? $%.2f/hr Net",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly());
                        case DAILY_100 -> String.format("Can You Make $100 a Day with %s in %s?",
                                        appName,
                                        city.getCityName());
                        case HOURLY_PAY -> String.format("%s %s Hourly Pay 2026: $%.2f/hr Net",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly());
                        case HOW_MUCH_CAN_YOU_MAKE -> String.format("How Much Can You Make with %s in %s?",
                                        appName,
                                        city.getCityName());
                        case BEST_AREAS -> String.format("Best Areas to DoorDash in %s: Pay Zones",
                                        city.getCityName());
                        case APP_COMPARISON -> String.format("Uber Eats vs DoorDash in %s: Which Pays More?",
                                        city.getCityName());
                        case MONTHLY_1000 -> String.format("Can %s Make $1,000/Month in %s?",
                                        appName,
                                        city.getCityName());
                        case NIGHTS_WEEKENDS -> appName.equals("DoorDash")
                                        ? String.format("Best Hours to DoorDash in %s: $%.2f/hr Net",
                                                        city.getCityName(),
                                                        scenario.getNetHourly())
                                        : String.format("%s %s Nights and Weekends: $%.2f/hr Net",
                                                        appName,
                                                        city.getCityName(),
                                                        scenario.getNetHourly());
                };
        }

        private String buildCityIntentDescription(
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario,
                        String monthYear) {
                return switch (intentPage) {
                        case AFTER_GAS -> String.format(
                                        "%s %s after gas and mileage: $%.2f/hr net on a 25-hour baseline, with fuel, miles, tax, and driver field notes. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case PER_MILE -> String.format(
                                        "%s %s pay per mile reality check: compare $%d/week gross, %d miles, and $%.2f/hr net before accepting low-mileage offers. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        scenario.getGrossWeekly(),
                                        scenario.getMiles(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case ACTIVE_TIME -> String.format(
                                        "%s %s active-time pay estimate: $%.2f/hr net after mileage and tax, with online-time and dash-time checks. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case WORTH_IT -> String.format(
                                        "Is %s worth it in %s after expenses? Side-hustle estimate: $%.2f/hr net after mileage and SE tax. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case DAILY_100 -> String.format(
                                        "%s %s $100/day estimate: about %.1f hours and %d miles to clear $100 net after mileage and tax. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        hoursToNetTarget(scenario, 100),
                                        milesForHours(scenario, hoursToNetTarget(scenario, 100)),
                                        monthYear);
                        case HOURLY_PAY -> String.format(
                                        "%s %s hourly pay estimate: $%.2f/hr net after mileage and tax, with gross weekly, miles, and public driver-shift evidence. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case HOW_MUCH_CAN_YOU_MAKE -> String.format(
                                        "How much can you make with %s in %s? Compare $%d/week gross, $%.2f/hr net, $100/day math, and mileage pressure. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        scenario.getGrossWeekly(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case BEST_AREAS -> String.format(
                                        "Best areas to DoorDash in %s: compare local demand zones, mileage pressure, $%.2f/hr net pay, and what to check before dashing. Updated %s.",
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case APP_COMPARISON -> String.format(
                                        "Uber Eats vs DoorDash in %s: compare delivery-app net hourly pay, active-time risk, mileage, and city-specific shift evidence. Updated %s.",
                                        city.getCityName(),
                                        monthYear);
                        case MONTHLY_1000 -> String.format(
                                        "%s %s $1,000/month estimate: about %.1f hours per week at the current $%.2f/hr net baseline. Updated %s.",
                                        appName,
                                        city.getCityName(),
                                        hoursToNetTarget(scenario, 1000 / 4.33),
                                        scenario.getNetHourly(),
                                        monthYear);
                        case NIGHTS_WEEKENDS -> appName.equals("DoorDash")
                                        ? String.format(
                                                        "Best hours to DoorDash in %s: a 12-hour nights/weekends plan models about $%.0f net after mileage and tax. Updated %s.",
                                                        city.getCityName(),
                                                        scenario.getNetHourly() * 12,
                                                        monthYear)
                                        : String.format(
                                                        "%s %s nights and weekends estimate: a 12-hour weekend pace models about $%.0f net after mileage and tax. Updated %s.",
                                                        appName,
                                                        city.getCityName(),
                                                        scenario.getNetHourly() * 12,
                                                        monthYear);
                };
        }

        private String buildCityIntentAnswerHtml(
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario) {
                CityLocalData localData = dataLayerService.getLocalData(city.getSlug());
                return switch (intentPage) {
                        case AFTER_GAS -> String.format(
                                        "<p>After gas, mileage, and self-employment tax assumptions, the %s side-hustle baseline in %s is <strong>$%.2f/hr net</strong>. The model starts from <strong>$%d/week gross</strong>, <strong>%d miles/week</strong>, <strong>%d hours/week</strong>, and local gas around <strong>$%.2f/gal</strong>.</p><p>The key driver check is whether your actual route mix stays near this mileage load. If restaurant waits, airport queues, or suburb returns add miles without pay, the after-gas number drops before the app payout looks bad.</p>",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        scenario.getGrossWeekly(),
                                        scenario.getMiles(),
                                        scenario.getHours(),
                                        city.getGasPrice());
                        case PER_MILE -> String.format(
                                        "<p>The %s %s side-hustle baseline uses <strong>$%d/week gross</strong> over <strong>%d miles</strong>, then converts the result into <strong>$%.2f/hr net</strong> after mileage and tax assumptions.</p><p>For drivers, the practical question is not only hourly pay. A shift can look good by the hour and still fail a dollar-per-mile floor if it sends you across zones, into deadhead miles, or back home unpaid.</p>",
                                        appName,
                                        city.getCityName(),
                                        scenario.getGrossWeekly(),
                                        scenario.getMiles(),
                                        scenario.getNetHourly());
                        case ACTIVE_TIME -> String.format(
                                        "<p>The %s %s estimate is based on <strong>%d hours/week</strong>, but drivers should compare that against active time, online time, dash time, and waiting time. The current side-hustle baseline is <strong>$%.2f/hr net</strong>.</p><p>If the app shows strong active-time earnings but you spent extra time waiting, repositioning, or driving home, your real hourly rate is lower than the active-time screenshot.</p>",
                                        appName,
                                        city.getCityName(),
                                        scenario.getHours(),
                                        scenario.getNetHourly());
                        case WORTH_IT -> String.format(
                                        "<p>%s in %s looks worth testing when your real shifts can stay near the side-hustle baseline: <strong>$%d/week gross</strong>, <strong>%d miles</strong>, <strong>%d hours</strong>, and about <strong>$%.2f/hr net</strong> after expenses.</p><p>It is less attractive if your market timing forces long waits, low-tip orders, long pickups, or high deadhead miles. Use the field notes below before treating this as a guaranteed wage.</p>",
                                        appName,
                                        city.getCityName(),
                                        scenario.getGrossWeekly(),
                                        scenario.getMiles(),
                                        scenario.getHours(),
                                        scenario.getNetHourly());
                        case DAILY_100 -> {
                                double targetHours = hoursToNetTarget(scenario, 100);
                                int targetMiles = milesForHours(scenario, targetHours);
                                int targetGross = grossForHours(scenario, targetHours);
                                yield String.format(
                                                "<p>At the current %s %s side-hustle baseline of <strong>$%.2f/hr net</strong>, a driver would need about <strong>%.1f hours</strong>, roughly <strong>%d miles</strong>, and about <strong>$%d gross</strong> to clear <strong>$100 net</strong> after mileage and tax assumptions.</p><p>This target gets harder when the app is slow, tips are weak, or a route forces unpaid return miles. Treat the $100/day number as a shift plan, not a guaranteed app promise.</p>",
                                                appName,
                                                city.getCityName(),
                                                scenario.getNetHourly(),
                                                targetHours,
                                                targetMiles,
                                                targetGross);
                        }
                        case HOURLY_PAY -> String.format(
                                        "<p>The practical %s hourly pay estimate in %s is <strong>$%.2f/hr net</strong> after mileage and self-employment tax assumptions. The modeled shift base is <strong>$%d/week gross</strong>, <strong>%d hours/week</strong>, and <strong>%d miles/week</strong>, so this page treats hourly pay as take-home math, not an app screenshot.</p><p>Use this page when searching <strong>%s hourly pay %s</strong> and then sanity-check it against the public shift evidence below, including reports that separate active time, dash time, gross payout, and mileage.</p>",
                                        appName,
                                        city.getCityName(),
                                        scenario.getNetHourly(),
                                        scenario.getGrossWeekly(),
                                        scenario.getHours(),
                                        scenario.getMiles(),
                                        appName,
                                        city.getCityName());
                        case HOW_MUCH_CAN_YOU_MAKE -> {
                                double targetHours = hoursToNetTarget(scenario, 100);
                                int targetMiles = milesForHours(scenario, targetHours);
                                double monthlyNet = scenario.getNetProfit() * 4.33;
                                yield String.format(
                                                "<p>At the current %s %s side-hustle baseline, the model shows about <strong>$%d/week gross</strong>, <strong>$%.0f/week net</strong>, and <strong>$%.0f/month net</strong> before you change the calculator inputs.</p><p>For the common question <strong>how much can you make with %s in %s</strong>, the sharper answer is this: a <strong>$100 net day</strong> requires about <strong>%.1f hours</strong> and roughly <strong>%d miles</strong> at the current baseline. If your actual shift has weaker tips, longer pickups, or unpaid return miles, the number falls fast.</p>",
                                                appName,
                                                city.getCityName(),
                                                scenario.getGrossWeekly(),
                                                scenario.getNetProfit(),
                                                monthlyNet,
                                                appName,
                                                city.getCityName(),
                                                targetHours,
                                                targetMiles);
                        }
                        case BEST_AREAS -> String.format(
                                        "<p>The best areas to DoorDash in %s are usually not the whole city. Start by testing restaurant density around <strong>%s</strong> and retail/order volume near <strong>%s</strong>, then compare the result against the modeled <strong>$%.2f/hr net</strong> baseline.</p><p>The trap is crossing too many zones or using <strong>%s</strong> as unpaid repositioning. A good DoorDash area should reduce pickup waits, keep drop-offs close enough to return quickly, and avoid turning a decent gross payout into a high-mileage shift.</p>",
                                        city.getCityName(),
                                        localData.nightlifeDistrict(),
                                        localData.shoppingDistrict(),
                                        scenario.getNetHourly(),
                                        localData.majorHighway());
                        case APP_COMPARISON -> {
                                CityScenario uberScenario = generateScenarioByWorkLevel(city, "uber", WorkLevel.SIDE_HUSTLE);
                                CityScenario doordashScenario = generateScenarioByWorkLevel(city, "doordash",
                                                WorkLevel.SIDE_HUSTLE);
                                double gap = doordashScenario.getNetHourly() - uberScenario.getNetHourly();
                                String leader = gap >= 0 ? "DoorDash" : "Uber/Uber Eats-style driving";
                                yield String.format(
                                                "<p>For an <strong>Uber Eats vs DoorDash in %s</strong> comparison, use net hourly and mileage pressure first. The current DoorDash side-hustle model is <strong>$%.2f/hr net</strong>, while the Uber city model is <strong>$%.2f/hr net</strong>. On this model, <strong>%s leads by $%.2f/hr</strong>.</p><p>This does not mean one app always wins. DoorDash can win when restaurant density around %s keeps pickup time low; Uber or Uber Eats-style work can win when trip flow, airport timing, or stacked demand reduces idle time. The right comparison is the shift you can actually run in %s.</p>",
                                                city.getCityName(),
                                                doordashScenario.getNetHourly(),
                                                uberScenario.getNetHourly(),
                                                leader,
                                                Math.abs(gap),
                                                localData.shoppingDistrict(),
                                                city.getCityName());
                        }
                        case MONTHLY_1000 -> {
                                double weeklyTarget = 1000 / 4.33;
                                double targetHours = hoursToNetTarget(scenario, weeklyTarget);
                                int targetMiles = milesForHours(scenario, targetHours);
                                yield String.format(
                                                "<p>To clear <strong>$1,000/month net</strong> with %s in %s, the model requires about <strong>$%.0f/week net</strong>. At <strong>$%.2f/hr net</strong>, that is roughly <strong>%.1f hours/week</strong> and <strong>%d miles/week</strong>.</p><p>The target is most realistic when you can concentrate hours into stronger windows instead of stretching the same miles across slow shifts.</p>",
                                                appName,
                                                city.getCityName(),
                                                weeklyTarget,
                                                scenario.getNetHourly(),
                                                targetHours,
                                                targetMiles);
                        }
                        case NIGHTS_WEEKENDS -> {
                                int weekendHours = 12;
                                int weekendMiles = milesForHours(scenario, weekendHours);
                                int weekendGross = grossForHours(scenario, weekendHours);
                                double weekendNet = scenario.getNetHourly() * weekendHours;
                                if (appName.equals("DoorDash")) {
                                        yield String.format(
                                                        "<p>The best hours to DoorDash in %s are usually dinner, weekend, late-night, event, or dense lunch windows where pickup waits stay low. A <strong>%d-hour nights/weekends plan</strong> at the current baseline models about <strong>$%.0f net</strong>, <strong>$%d gross</strong>, and <strong>%d miles</strong>.</p><p>If those hours spill into slow gaps, the active-time screenshot can still look decent while the real all-in hourly rate falls. Treat this as a schedule test, not just a citywide average.</p>",
                                                        city.getCityName(),
                                                        weekendHours,
                                                        weekendNet,
                                                        weekendGross,
                                                        weekendMiles);
                                }
                                yield String.format(
                                                "<p>A nights-and-weekends plan for %s in %s works best when the driver can compress demand into dinner, late-night, event, or airport windows. A <strong>%d-hour weekend</strong> at the current baseline models about <strong>$%.0f net</strong>, <strong>$%d gross</strong>, and <strong>%d miles</strong>.</p><p>If those hours spill into slow gaps, the active-time screenshot can still look decent while the real all-in hourly rate falls.</p>",
                                                appName,
                                                city.getCityName(),
                                                weekendHours,
                                                weekendNet,
                                                weekendGross,
                                                weekendMiles);
                        }
                };
        }

        private double hoursToNetTarget(CityScenario scenario, double netTarget) {
                if (scenario.getNetHourly() <= 0) {
                        return 0;
                }
                return netTarget / scenario.getNetHourly();
        }

        private int milesForHours(CityScenario scenario, double hours) {
                if (scenario.getHours() <= 0) {
                        return 0;
                }
                return (int) Math.round((scenario.getMiles() / (double) scenario.getHours()) * hours);
        }

        private int grossForHours(CityScenario scenario, double hours) {
                if (scenario.getHours() <= 0) {
                        return 0;
                }
                return (int) Math.round((scenario.getGrossWeekly() / (double) scenario.getHours()) * hours);
        }

        private List<SearchResultPattern> buildSearchResultPatterns(
                        String app,
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario) {
                String intentLabel = intentPage == null ? "city earnings" : intentPage.getSearchPhrase();
                String officialPayLabel = app.equals("doordash")
                                ? "DoorDash official pay docs"
                                : "Uber official pay docs";
                String officialPayUrl = app.equals("doordash")
                                ? "https://dasher.doordash.com/en-us/about/pay"
                                : "https://www.uber.com/us/en/drive/how-much-drivers-make/";
                String officialCoverageLabel = app.equals("doordash")
                                ? "DoorDash Dasher signup"
                                : "Uber official city directory";
                String officialCoverageUrl = app.equals("doordash")
                                ? "https://dasher.doordash.com/en-us"
                                : "https://www.uber.com/us/en/e/drive/cities/";
                String fieldTestLabel = app.equals("doordash")
                                ? "NerdWallet DoorDash field test"
                                : "NerdWallet Uber field test";
                String fieldTestUrl = app.equals("doordash")
                                ? "https://www.nerdwallet.com/finance/learn/how-much-does-doordash-pay"
                                : "https://www.nerdwallet.com/finance/learn/how-much-does-an-uber-driver-make";
                String gridwiseLabel = app.equals("doordash")
                                ? "Gridwise DoorDash pay data"
                                : "Gridwise Uber pay data";
                String gridwiseUrl = app.equals("doordash")
                                ? "https://gridwise.io/blog/how-much-do-doordash-drivers-make"
                                : "https://gridwise.io/blog/how-much-do-uber-drivers-make";
                String expertLabel = app.equals("doordash")
                                ? "DoorDash pay model guide"
                                : "The Rideshare Guy Uber guide";
                String expertUrl = app.equals("doordash")
                                ? "https://help.doordash.com/en-us/dashers/article/how-is-dasher-pay-calculated"
                                : "https://therideshareguy.com/how-much-do-uber-drivers-make/";
                String salaryLabel = app.equals("doordash")
                                ? "Indeed DoorDash salary data"
                                : "Indeed Uber Driver salary data";
                String salaryUrl = app.equals("doordash")
                                ? "https://www.indeed.com/cmp/Doordash/salaries/Delivery-Driver"
                                : "https://www.indeed.com/cmp/Uber-Drivers/salaries/Driver";
                String discussionLabel = app.equals("doordash")
                                ? "DoorDash driver discussion"
                                : "Uber driver discussion";
                String discussionUrl = app.equals("doordash")
                                ? "https://www.reddit.com/r/doordash_drivers/comments/1tydq5v/is_doordash_still_worth_it_in_2026/"
                                : "https://www.reddit.com/r/uberdrivers/comments/1paewc1/how_much_do_you_make_driving_for_uber/";
                String tacticalLabel = app.equals("doordash")
                                ? "ShiftTracker DoorDash pay guide"
                                : "ShiftTracker Uber driver guide";
                String tacticalUrl = app.equals("doordash")
                                ? "https://shifttrackerapp.com/blog/how-do-doordash-drivers-get-paid-2026"
                                : "https://shifttrackerapp.com/blog/uber-eats-uber-driver-guide-2026-boost-pay-track-expenses-and-work-smarter";
                String policyLabel = app.equals("doordash")
                                ? "PayUp DoorDash report"
                                : "Seattle Driver Union pay calculator";
                String policyUrl = app.equals("doordash")
                                ? "https://payup.wtf/doordash/no-free-lunch-report"
                                : "https://www.driversunionwa.org/pay-calculator";
                String videoLabel = app.equals("doordash")
                                ? "DoorDash pay creator SERP"
                                : "Uber pay creator SERP";
                String videoUrl = app.equals("doordash")
                                ? "https://www.youtube.com/results?search_query=how+much+do+doordash+drivers+make+2026"
                                : "https://www.youtube.com/results?search_query=how+much+do+uber+drivers+make+2026";

                return List.of(
                                new SearchResultPattern(
                                                "Official pay docs",
                                                officialPayLabel,
                                                "Ranks because it is the source of truth for pay components.",
                                                String.format(
                                                                "Official pages explain how %s pay works, but they do not answer whether %s in %s clears expenses.",
                                                                appName,
                                                                intentLabel,
                                                                city.getCityName()),
                                                String.format(
                                                                "Keep the official pay components visible, then convert the %s baseline into $%.2f/hr net after mileage and self-employment tax.",
                                                                city.getCityName(),
                                                                scenario.getNetHourly()),
                                                officialPayLabel,
                                                officialPayUrl),
                                new SearchResultPattern(
                                                "Coverage docs",
                                                officialCoverageLabel,
                                                "Wins availability intent before any earnings estimate matters.",
                                                String.format(
                                                                "Coverage pages confirm whether the platform is active, but they do not model a %d-mile week, local gas, or take-home pay.",
                                                                scenario.getMiles()),
                                                String.format(
                                                                "Link users to the official availability check, then bring them back to the %s earnings model and calculator.",
                                                                city.getCityName()),
                                                officialCoverageLabel,
                                                officialCoverageUrl),
                                new SearchResultPattern(
                                                "Field test",
                                                fieldTestLabel,
                                                "Wins trust by showing a real drive instead of a generic average.",
                                                "One field test is vivid, but it is not a city-by-city planning system and may not match this market, schedule, or vehicle.",
                                                String.format(
                                                                "Use field-test friction as evidence, while keeping the %s page specific to $%d gross, %d miles, and %d hours.",
                                                                city.getCityName(),
                                                                scenario.getGrossWeekly(),
                                                                scenario.getMiles(),
                                                                scenario.getHours()),
                                                fieldTestLabel,
                                                fieldTestUrl),
                                new SearchResultPattern(
                                                "Large dataset",
                                                gridwiseLabel,
                                                "Wins by aggregating many driver records and showing platform-level direction.",
                                                String.format(
                                                                "Large app-wide datasets are strong, but they usually flatten %s timing, traffic, and vehicle-cost differences.",
                                                                city.getCityName()),
                                                "Use large-data context as a sanity check, then expose local net hourly, per-mile, and target-hour calculations.",
                                                gridwiseLabel,
                                                gridwiseUrl),
                                new SearchResultPattern(
                                                "Expert guide",
                                                expertLabel,
                                                "Wins because experienced driver publishers explain tactics and pay ranges.",
                                                "Expert guides are useful, but broad app advice often stops before a city-specific after-expenses answer.",
                                                String.format(
                                                                "Translate the broad tactic into a %s planning page: hours, miles, gross needed, tax pressure, and related intent links.",
                                                                city.getCityName()),
                                                expertLabel,
                                                expertUrl),
                                new SearchResultPattern(
                                                "Salary aggregate",
                                                salaryLabel,
                                                "Wins freshness and volume with constantly updated salary snippets.",
                                                "Salary aggregators mix regions, worker types, and reporting methods, and rarely separate gross pay from 1099 costs.",
                                                String.format(
                                                                "Show a transparent 1099 model instead: $%.2f/hr net after the 2026 mileage proxy and self-employment tax.",
                                                                scenario.getNetHourly()),
                                                salaryLabel,
                                                salaryUrl),
                                new SearchResultPattern(
                                                "Driver forum",
                                                discussionLabel,
                                                "Wins because drivers search for current pain, saturation, and real shift stories.",
                                                "Forum answers are fast and candid, but they are noisy, unstructured, and often hard to compare across cities.",
                                                String.format(
                                                                "Turn the driver pain points into structured checks for %s: active time, dead miles, target hours, and market timing.",
                                                                city.getCityName()),
                                                discussionLabel,
                                                discussionUrl),
                                new SearchResultPattern(
                                                "Tactical app blog",
                                                tacticalLabel,
                                                "Wins with practical 2026 advice around boosts, tracking, and expenses.",
                                                "Tactical posts help drivers operate, but they usually do not give a clean city URL for a single earnings question.",
                                                String.format(
                                                                "Make the page match one high-intent query, then point users into the calculator when their %s routine differs.",
                                                                city.getCityName()),
                                                tacticalLabel,
                                                tacticalUrl),
                                new SearchResultPattern(
                                                "Policy report",
                                                policyLabel,
                                                "Wins when the user wants take-rate, labor, and fairness context.",
                                                "Policy pages explain why driver pay can feel broken, but they are usually not a shift-level earnings planner.",
                                                "Keep policy and take-rate pressure as context while answering the practical net-pay question on the page.",
                                                policyLabel,
                                                policyUrl),
                                new SearchResultPattern(
                                                "Creator SERP",
                                                videoLabel,
                                                "Wins attention with screenshots, daily goals, and relatable shift narratives.",
                                                "Videos are persuasive, but they are slow to scan and rarely expose a reusable city calculator or source trail.",
                                                String.format(
                                                                "Answer the creator-style money question in text: whether %s can reach the target, how many hours it takes, and what miles it costs.",
                                                                city.getCityName()),
                                                videoLabel,
                                                videoUrl));
        }

        private List<CityIntentEvidence> buildCityIntentEvidencePatterns(
                        String app,
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario) {
                double weeklyFuelCost = (scenario.getMiles() / 25.0) * city.getGasPrice();
                double mileageProxy = scenario.getMiles() * AppConstants.IRS_MILEAGE_RATE;
                double grossPerMile = scenario.getMiles() == 0 ? 0
                                : scenario.getGrossWeekly() / (double) scenario.getMiles();
                double netPerMile = scenario.getMiles() == 0 ? 0
                                : scenario.getNetProfit() / scenario.getMiles();
                double allInHours = scenario.getHours() * 1.2;
                double allInHourly = allInHours == 0 ? 0 : scenario.getNetProfit() / allInHours;
                CityLocalData localData = dataLayerService.getLocalData(city.getSlug());

                String fieldTestLabel = app.equals("doordash")
                                ? "NerdWallet DoorDash field test"
                                : "NerdWallet Uber field test";
                String fieldTestUrl = app.equals("doordash")
                                ? "https://www.nerdwallet.com/finance/learn/how-much-does-doordash-pay"
                                : "https://www.nerdwallet.com/finance/learn/how-much-does-an-uber-driver-make";
                String platformClockLabel = app.equals("doordash")
                                ? "DoorDash Earn by Time help"
                                : "Uber earnings guide";
                String platformClockUrl = app.equals("doordash")
                                ? "https://help.doordash.com/en-us/dashers/article/time-earnings-mode"
                                : "https://www.uber.com/us/en/deliver/earnings/";
                String gridwiseLabel = app.equals("doordash")
                                ? "Gridwise DoorDash 2026 pay data"
                                : "Gridwise Uber 2026 pay data";
                String gridwiseUrl = app.equals("doordash")
                                ? "https://gridwise.io/blog/how-much-do-doordash-drivers-make"
                                : "https://gridwise.io/blog/how-much-do-uber-drivers-make";
                String mileThreadLabel = app.equals("doordash")
                                ? "DoorDash driver $/mile discussion"
                                : "Uber driver $/mile discussion";
                String mileThreadUrl = app.equals("doordash")
                                ? "https://www.reddit.com/r/doordash_drivers/comments/1s39uhj/whats_your_minimum_mile_to_accept_doordash_orders/"
                                : "https://www.reddit.com/r/uberdrivers/comments/1s39ymq/whats_your_minimum_mile_to_accept_orders_in_2026/";
                String worthItThreadLabel = app.equals("doordash")
                                ? "DoorDash 2026 worth-it discussion"
                                : "Uber 2026 worth-it discussion";
                String worthItThreadUrl = app.equals("doordash")
                                ? "https://www.reddit.com/r/doordash_drivers/comments/1rs4kmu/is_it_worth_coming_back_to_dashing_in_2026/"
                                : "https://www.reddit.com/r/UberEatsDrivers/comments/1q391xs/is_it_worth_being_an_uber_driver_in_2026/";
                String dailyTargetLabel = app.equals("doordash")
                                ? "DoorDash $100/day discussion"
                                : "Uber $100/day discussion";
                String dailyTargetUrl = app.equals("doordash")
                                ? "https://www.reddit.com/r/doordash_drivers/comments/1d2utnr/are_people_still_making_100_a_day/"
                                : "https://www.reddit.com/r/uberdrivers/comments/1qadhre/tired_of_driving_all_day_to_make_100/";
                String monthlyTargetLabel = app.equals("doordash")
                                ? "Part-time DoorDash weekly earnings"
                                : "Uber $1,000/month discussion";
                String monthlyTargetUrl = app.equals("doordash")
                                ? "https://www.reddit.com/r/doordash_drivers/comments/1hfrk97/how_much_do_you_make_weekly_as_a_parttime/"
                                : "https://www.reddit.com/r/uberdrivers/comments/1tc9lv0/realistically_how_much_driving_do_i_need_to_do_to/";
                String nightsWeekendLabel = app.equals("doordash")
                                ? "DoorDash nights/weekends discussion"
                                : "Uber nights/weekends discussion";
                String nightsWeekendUrl = app.equals("doordash")
                                ? "https://www.reddit.com/r/doordash_drivers/comments/1oaricl/how_much_can_i_make_on_nightsweekends/"
                                : "https://www.reddit.com/r/uberdrivers/comments/1p6unzz/how_much_money_do_yall_make/";

                return switch (intentPage) {
                        case AFTER_GAS -> List.of(
                                        new CityIntentEvidence(
                                                        "Field-test pattern",
                                                        "Gross screenshots need an expense filter",
                                                        String.format(
                                                                        "%s %s starts with $%.2f/hr net because field tests report app payout before fuel, vehicle wear, and tax. This page exposes the modeled $%.0f weekly fuel drag before the broader mileage proxy.",
                                                                        appName,
                                                                        city.getCityName(),
                                                                        scenario.getNetHourly(),
                                                                        weeklyFuelCost),
                                                        fieldTestLabel,
                                                        fieldTestUrl),
                                        new CityIntentEvidence(
                                                        "Cost benchmark",
                                                        "Mileage is larger than the pump receipt",
                                                        String.format(
                                                                        "The IRS 2026 business mileage benchmark is $0.725/mi. At %d modeled miles/week, that is about $%.0f of vehicle-cost pressure before self-employment tax.",
                                                                        scenario.getMiles(),
                                                                        mileageProxy),
                                                        "IRS 2026 mileage rate",
                                                        "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"),
                                        new CityIntentEvidence(
                                                        "Operating pattern",
                                                        "Dead miles and slow windows decide whether gas matters",
                                                        String.format(
                                                                        "Large driver datasets separate peak-window strategy from all-day grinding. That is why the %s page shows gross after fuel, miles/week, and the calculator link instead of only a single hourly headline.",
                                                                        city.getCityName()),
                                                        gridwiseLabel,
                                                        gridwiseUrl));
                        case PER_MILE -> List.of(
                                        new CityIntentEvidence(
                                                        "Acceptance floor",
                                                        "Drivers compare offers by mile before hourly math",
                                                        String.format(
                                                                        "%s %s is modeled at $%.2f gross/mi and $%.2f net/mi. That mirrors driver-source behavior: screen weak offers by dollars per mile before trusting an hourly total.",
                                                                        appName,
                                                                        city.getCityName(),
                                                                        grossPerMile,
                                                                        netPerMile),
                                                        mileThreadLabel,
                                                        mileThreadUrl),
                                        new CityIntentEvidence(
                                                        "Cost benchmark",
                                                        "A good offer still has to clear vehicle-cost pressure",
                                                        "The same $0.725/mi IRS proxy used across the site gives this page a hard comparison line. If the offer barely beats the cost proxy, the gross payout is not enough.",
                                                        "IRS 2026 mileage rate",
                                                        "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"),
                                        new CityIntentEvidence(
                                                        "Winner pattern",
                                                        "Top pSEO pages make the query-specific comparison visible",
                                                        "Good pSEO examples expose the exact data point the searcher came for. Here, the page puts gross-per-mile, net-per-mile, and the mileage proxy next to the source trail.",
                                                        gridwiseLabel,
                                                        gridwiseUrl));
                        case ACTIVE_TIME -> List.of(
                                        new CityIntentEvidence(
                                                        "Clock definition",
                                                        "Active time is not the same as total work time",
                                                        String.format(
                                                                        "%s documentation separates app clocks from total availability. This page stress-tests %d modeled hours as %.0f all-in hours to catch waiting and repositioning time.",
                                                                        appName,
                                                                        scenario.getHours(),
                                                                        allInHours),
                                                        platformClockLabel,
                                                        platformClockUrl),
                                        new CityIntentEvidence(
                                                        "Driver behavior",
                                                        "Online summaries need a waiting-time haircut",
                                                        String.format(
                                                                        "At a 20%% waiting buffer, the %s %s baseline drops to $%.2f/hr all-in. That is the number a driver should compare against screenshots that only show active or booked time.",
                                                                        appName,
                                                                        city.getCityName(),
                                                                        allInHourly),
                                                        gridwiseLabel,
                                                        gridwiseUrl),
                                        new CityIntentEvidence(
                                                        "Page uniqueness",
                                                        "The active-time page answers a different query than the city page",
                                                        "Instead of duplicating the city earnings page, this URL isolates the active-time risk: unpaid waiting, repositioning, and the clock definition used by the platform.",
                                                        platformClockLabel,
                                                        platformClockUrl));
                        case WORTH_IT -> List.of(
                                        new CityIntentEvidence(
                                                        "Decision pattern",
                                                        "Worth-it searches are really gross-vs-net checks",
                                                        String.format(
                                                                        "%s %s currently clears the side-hustle baseline at $%.2f/hr net, but the useful decision is whether your actual hours and miles stay close to the model.",
                                                                        appName,
                                                                        city.getCityName(),
                                                                        scenario.getNetHourly()),
                                                        worthItThreadLabel,
                                                        worthItThreadUrl),
                                        new CityIntentEvidence(
                                                        "Market timing",
                                                        "The same city can be good or bad by shift window",
                                                        "Driver-source data repeatedly points to peak windows, events, airport flows, and slow weekday gaps. The page links into city notes so the answer is not just a static average.",
                                                        gridwiseLabel,
                                                        gridwiseUrl),
                                        new CityIntentEvidence(
                                                        "Cost benchmark",
                                                        "A yes/no answer has to include mileage and tax",
                                                        String.format(
                                                                        "The modeled %d miles/week creates about $%.0f in IRS mileage proxy cost before tax. That is why the page routes users to the adjustable calculator after the direct answer.",
                                                                        scenario.getMiles(),
                                                                        mileageProxy),
                                                        "IRS 2026 mileage rate",
                                                        "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"));
                        case DAILY_100 -> {
                                double targetHours = hoursToNetTarget(scenario, 100);
                                int targetMiles = milesForHours(scenario, targetHours);
                                yield List.of(
                                                new CityIntentEvidence(
                                                                "Target-income pattern",
                                                                "$100/day questions are really hours-and-miles questions",
                                                                String.format(
                                                                                "Driver discussions frame $100 as possible but market-dependent. For %s %s, this page translates the target into %.1f modeled hours and %d miles instead of treating the number as a guarantee.",
                                                                                appName,
                                                                                city.getCityName(),
                                                                                targetHours,
                                                                                targetMiles),
                                                                dailyTargetLabel,
                                                                dailyTargetUrl),
                                                new CityIntentEvidence(
                                                                "Peak-window pattern",
                                                                "The target is easier in strong windows than all-day grinding",
                                                                "Driver-source comments repeatedly separate dinner, late-night, airport, event, and weekend windows from slow all-day availability. That is why this page links the target to hours and mileage.",
                                                                nightsWeekendLabel,
                                                                nightsWeekendUrl),
                                                new CityIntentEvidence(
                                                                "Cost benchmark",
                                                                "$100 gross is not $100 net",
                                                                String.format(
                                                                                "At %d modeled miles for a $100 net day, the IRS mileage proxy alone represents about $%.0f of vehicle-cost pressure before self-employment tax.",
                                                                                targetMiles,
                                                                                targetMiles * AppConstants.IRS_MILEAGE_RATE),
                                                                "IRS 2026 mileage rate",
                                                                "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"));
                        }
                        case HOURLY_PAY -> List.of(
                                        new CityIntentEvidence(
                                                        "Hourly-pay pattern",
                                                        "The ranking page has to answer net hourly, not only gross pay",
                                                        String.format(
                                                                        "%s %s shows $%.2f/hr net after mileage and tax because hourly-pay searches are usually trying to reconcile app screenshots with take-home money.",
                                                                        appName,
                                                                        city.getCityName(),
                                                                        scenario.getNetHourly()),
                                                        fieldTestLabel,
                                                        fieldTestUrl),
                                        new CityIntentEvidence(
                                                        "Published-shift pattern",
                                                        "Single shifts win trust when miles and clock time are visible",
                                                        String.format(
                                                                        "The public shift block below gives this URL a concrete cross-check for %d modeled miles/week and %d modeled hours/week instead of relying on a national average.",
                                                                        scenario.getMiles(),
                                                                        scenario.getHours()),
                                                        gridwiseLabel,
                                                        gridwiseUrl),
                                        new CityIntentEvidence(
                                                        "Cost benchmark",
                                                        "Hourly pay changes after the vehicle-cost proxy",
                                                        String.format(
                                                                        "At %d modeled miles/week, the IRS mileage proxy creates about $%.0f of weekly vehicle-cost pressure before tax.",
                                                                        scenario.getMiles(),
                                                                        mileageProxy),
                                                        "IRS 2026 mileage rate",
                                                        "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"));
                        case HOW_MUCH_CAN_YOU_MAKE -> {
                                double targetHours = hoursToNetTarget(scenario, 100);
                                int targetMiles = milesForHours(scenario, targetHours);
                                yield List.of(
                                                new CityIntentEvidence(
                                                                "Earnings-range pattern",
                                                                "How-much searches need weekly, daily, and hourly answers together",
                                                                String.format(
                                                                                "%s %s is modeled at $%d/week gross and $%.0f/week net, then translated into a %.1f-hour $100/day path.",
                                                                                appName,
                                                                                city.getCityName(),
                                                                                scenario.getGrossWeekly(),
                                                                                scenario.getNetProfit(),
                                                                                targetHours),
                                                                dailyTargetLabel,
                                                                dailyTargetUrl),
                                                new CityIntentEvidence(
                                                                "Mileage-load pattern",
                                                                "The answer is capped by miles, not only demand",
                                                                String.format(
                                                                                "The $100/day path implies about %d miles at the current city baseline. That is why this page keeps the mileage and calculator links visible.",
                                                                                targetMiles),
                                                                "IRS 2026 mileage rate",
                                                                "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"),
                                                new CityIntentEvidence(
                                                                "Driver-source pattern",
                                                                "Reported earnings vary because shifts are not equal",
                                                                "Driver discussions repeatedly separate strong dinner or weekend windows from slow all-day availability. This URL answers with a baseline and shows where that baseline can break.",
                                                                nightsWeekendLabel,
                                                                nightsWeekendUrl));
                        }
                        case BEST_AREAS -> List.of(
                                        new CityIntentEvidence(
                                                        "Zone-selection pattern",
                                                        "Best-area searches are local demand questions",
                                                        String.format(
                                                                        "For %s, the page names %s and %s because DoorDash performance depends on restaurant density, order batching, and short returns, not just citywide average pay.",
                                                                        city.getCityName(),
                                                                        localData.nightlifeDistrict(),
                                                                        localData.shoppingDistrict()),
                                                        "DoorDash Dasher signup",
                                                        "https://dasher.doordash.com/en-us"),
                                        new CityIntentEvidence(
                                                        "Mileage pattern",
                                                        "The wrong zone turns pay into dead miles",
                                                        String.format(
                                                                        "The modeled side-hustle week already carries %d miles. A better DoorDash area should keep pickups close and avoid using %s as unpaid repositioning.",
                                                                        scenario.getMiles(),
                                                                        localData.majorHighway()),
                                                        mileThreadLabel,
                                                        mileThreadUrl),
                                        new CityIntentEvidence(
                                                        "Shift-evidence pattern",
                                                        "Good area advice needs real shift friction",
                                                        "The published-shift cards below keep the page from becoming generic local advice by tying zone selection back to active time, dash time, gross pay, and mileage.",
                                                        fieldTestLabel,
                                                        fieldTestUrl));
                        case APP_COMPARISON -> {
                                CityScenario uberScenario = generateScenarioByWorkLevel(city, "uber", WorkLevel.SIDE_HUSTLE);
                                CityScenario doordashScenario = generateScenarioByWorkLevel(city, "doordash",
                                                WorkLevel.SIDE_HUSTLE);
                                String comparisonUrl = pageIndexPolicyService.isCityReportIndexable(city, "uber")
                                                && pageIndexPolicyService.isCityReportIndexable(city, "doordash")
                                                ? String.format("%s/compare/%s/uber-vs-doordash",
                                                                AppConstants.BASE_URL,
                                                                city.getSlug())
                                                : AppConstants.BASE_URL + "/blog/uber-vs-doordash";
                                yield List.of(
                                                new CityIntentEvidence(
                                                                "Comparison pattern",
                                                                "The winner is the app with stronger net hourly after miles",
                                                                String.format(
                                                                                "DoorDash models at $%.2f/hr net in %s versus $%.2f/hr for the Uber city model. The useful comparison is net pay after vehicle cost, not the highest gross screenshot.",
                                                                                doordashScenario.getNetHourly(),
                                                                                city.getCityName(),
                                                                                uberScenario.getNetHourly()),
                                                                "GigVerdict city compare page",
                                                                comparisonUrl),
                                                new CityIntentEvidence(
                                                                "Clock-risk pattern",
                                                                "Uber Eats and DoorDash both overstate pay if waiting time is ignored",
                                                                "Official pay docs explain pay components, but the driver decision needs online time, active time, dash time, and idle gaps in the same comparison.",
                                                                platformClockLabel,
                                                                platformClockUrl),
                                                new CityIntentEvidence(
                                                                "Local-fit pattern",
                                                                "Restaurant density can beat broad app averages",
                                                                String.format(
                                                                                "DoorDash can outperform when %s and %s produce short pickups; Uber or Uber Eats-style work can outperform when trip flow and airport timing reduce idle time.",
                                                                                localData.nightlifeDistrict(),
                                                                                localData.shoppingDistrict()),
                                                                gridwiseLabel,
                                                                gridwiseUrl));
                        }
                        case MONTHLY_1000 -> {
                                double weeklyTarget = 1000 / 4.33;
                                double targetHours = hoursToNetTarget(scenario, weeklyTarget);
                                int targetMiles = milesForHours(scenario, targetHours);
                                yield List.of(
                                                new CityIntentEvidence(
                                                                "Monthly target pattern",
                                                                "$1,000/month needs a weekly schedule, not one lucky day",
                                                                String.format(
                                                                                "Driver discussions about $1,000/month usually come down to schedule flexibility, market strength, and vehicle cost. For %s %s, the modeled target is %.1f hours/week.",
                                                                                appName,
                                                                                city.getCityName(),
                                                                                targetHours),
                                                                monthlyTargetLabel,
                                                                monthlyTargetUrl),
                                                new CityIntentEvidence(
                                                                "Mileage load",
                                                                "A monthly side-income goal still wears the vehicle",
                                                                String.format(
                                                                                "The $1,000/month plan implies about %d miles/week at the current city baseline. That is why this URL is separate from the generic city earnings page.",
                                                                                targetMiles),
                                                                "IRS 2026 mileage rate",
                                                                "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"),
                                                new CityIntentEvidence(
                                                                "Market timing",
                                                                "Side-income targets are more realistic when hours are concentrated",
                                                                "Large driver datasets and field reports point to timing, not just city averages. A $1,000/month target is more plausible when the driver can choose stronger windows.",
                                                                gridwiseLabel,
                                                                gridwiseUrl));
                        }
                        case NIGHTS_WEEKENDS -> {
                                int weekendHours = 12;
                                int weekendMiles = milesForHours(scenario, weekendHours);
                                yield List.of(
                                                new CityIntentEvidence(
                                                                "Schedule pattern",
                                                                "Nights and weekends are a different job than all-day driving",
                                                                String.format(
                                                                                "Driver discussions separate weekend/night performance from weekday availability. For %s %s, this page models a %d-hour weekend and its %d-mile load.",
                                                                                appName,
                                                                                city.getCityName(),
                                                                                weekendHours,
                                                                                weekendMiles),
                                                                nightsWeekendLabel,
                                                                nightsWeekendUrl),
                                                new CityIntentEvidence(
                                                                "Demand pattern",
                                                                "Late windows help only if dead time stays low",
                                                                "The useful check is whether dinner, late-night, airport, event, or bar-close demand offsets waiting and repositioning. Otherwise the shift can look good by active time and weak by all-in time.",
                                                                platformClockLabel,
                                                                platformClockUrl),
                                                new CityIntentEvidence(
                                                                "Cost benchmark",
                                                                "Weekend miles still count",
                                                                String.format(
                                                                                "A %d-mile weekend has about $%.0f of IRS mileage proxy cost before tax. The page keeps that number visible so weekend earnings do not become gross-only content.",
                                                                                weekendMiles,
                                                                                weekendMiles * AppConstants.IRS_MILEAGE_RATE),
                                                                "IRS 2026 mileage rate",
                                                                "https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents"));
                        }
                };
        }

        private List<CityIntentMetric> buildCityIntentMetrics(
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario) {
                double weeklyFuelCost = (scenario.getMiles() / 25.0) * city.getGasPrice();
                double grossPerMile = scenario.getMiles() == 0 ? 0 : scenario.getGrossWeekly() / (double) scenario.getMiles();
                double netPerMile = scenario.getMiles() == 0 ? 0 : scenario.getNetProfit() / scenario.getMiles();
                double allInHours = scenario.getHours() * 1.2;
                double allInHourly = allInHours == 0 ? 0 : scenario.getNetProfit() / allInHours;
                double monthlyNet = scenario.getNetProfit() * 4.33;
                double minWageGap = scenario.getNetHourly() - city.getMinWage();
                double hoursToHundred = hoursToNetTarget(scenario, 100);
                double weeklyTargetForThousand = 1000 / 4.33;
                double hoursToThousandMonthly = hoursToNetTarget(scenario, weeklyTargetForThousand);
                int weekendHours = 12;
                CityLocalData localData = dataLayerService.getLocalData(city.getSlug());

                return switch (intentPage) {
                        case AFTER_GAS -> List.of(
                                        new CityIntentMetric("Estimated weekly fuel", String.format("$%.0f", weeklyFuelCost),
                                                        String.format("Uses %d miles/week, 25 MPG, and $%.2f/gal local gas.",
                                                                        scenario.getMiles(),
                                                                        city.getGasPrice())),
                                        new CityIntentMetric("Gross after fuel", String.format("$%.0f",
                                                        scenario.getGrossWeekly() - weeklyFuelCost),
                                                        "This is before mileage depreciation proxy and self-employment tax."),
                                        new CityIntentMetric("Net hourly", String.format("$%.2f/hr", scenario.getNetHourly()),
                                                        "The model still uses the IRS mileage proxy because gas is only one vehicle cost."));
                        case PER_MILE -> List.of(
                                        new CityIntentMetric("Gross per mile", String.format("$%.2f/mi", grossPerMile),
                                                        "Gross payout divided by modeled weekly miles."),
                                        new CityIntentMetric("Net profit per mile", String.format("$%.2f/mi", netPerMile),
                                                        "Net profit after mileage and tax assumptions divided by modeled weekly miles."),
                                        new CityIntentMetric("IRS mileage proxy", "$0.725/mi",
                                                        "A benchmark for vehicle operating cost pressure in 2026."));
                        case ACTIVE_TIME -> List.of(
                                        new CityIntentMetric("Modeled hours", scenario.getHours() + " hrs/wk",
                                                        "The baseline hour count used in this city estimate."),
                                        new CityIntentMetric("20% waiting buffer", String.format("%.0f hrs/wk", allInHours),
                                                        "Stress test for online or dash time exceeding active work time."),
                                        new CityIntentMetric("All-in hourly stress test", String.format("$%.2f/hr", allInHourly),
                                                        "Net profit divided by modeled hours plus the 20% waiting buffer."));
                        case WORTH_IT -> List.of(
                                        new CityIntentMetric("Monthly net estimate", String.format("$%.0f", monthlyNet),
                                                        "Weekly net profit multiplied by 4.33 weeks."),
                                        new CityIntentMetric("Local min wage gap", String.format("%s$%.2f/hr",
                                                        minWageGap >= 0 ? "+" : "-",
                                                        Math.abs(minWageGap)),
                                                        "Net hourly estimate compared with the local minimum wage."),
                                        new CityIntentMetric("Annual mileage load", String.format("%,d mi",
                                                        scenario.getMiles() * 52),
                                                        "A side-hustle schedule can still add serious vehicle wear over a year."));
                        case DAILY_100 -> List.of(
                                        new CityIntentMetric("Hours to $100 net", String.format("%.1f hrs", hoursToHundred),
                                                        "Net target divided by the current city net hourly baseline."),
                                        new CityIntentMetric("Miles to $100 net", String.format("%d mi",
                                                        milesForHours(scenario, hoursToHundred)),
                                                        "Modeled mileage load at the hours needed for a $100 net day."),
                                        new CityIntentMetric("Gross needed", String.format("$%d",
                                                        grossForHours(scenario, hoursToHundred)),
                                                        "Approximate app payout before mileage proxy and self-employment tax."));
                        case HOURLY_PAY -> List.of(
                                        new CityIntentMetric("Net hourly", String.format("$%.2f/hr",
                                                        scenario.getNetHourly()),
                                                        "Take-home estimate after mileage and self-employment tax assumptions."),
                                        new CityIntentMetric("Gross hourly", String.format("$%.2f/hr",
                                                        scenario.getGrossWeekly() / (double) scenario.getHours()),
                                                        "App payout baseline before vehicle-cost and tax assumptions."),
                                        new CityIntentMetric("Miles per hour", String.format("%.1f mi/hr",
                                                        scenario.getMiles() / (double) scenario.getHours()),
                                                        "Mileage intensity behind the hourly pay estimate."));
                        case HOW_MUCH_CAN_YOU_MAKE -> List.of(
                                        new CityIntentMetric("Weekly net estimate", String.format("$%.0f",
                                                        scenario.getNetProfit()),
                                                        "Modeled weekly take-home after mileage and tax assumptions."),
                                        new CityIntentMetric("Monthly net estimate", String.format("$%.0f",
                                                        monthlyNet),
                                                        "Weekly net multiplied by 4.33 average weeks."),
                                        new CityIntentMetric("Hours to $100 net", String.format("%.1f hrs",
                                                        hoursToHundred),
                                                        "Daily target divided by this city's current net hourly baseline."));
                        case BEST_AREAS -> List.of(
                                        new CityIntentMetric("Primary demand zone", localData.nightlifeDistrict(),
                                                        "Use this as a first test for restaurant and evening order density."),
                                        new CityIntentMetric("Retail/order zone", localData.shoppingDistrict(),
                                                        "Check whether retail and restaurant clusters reduce pickup dead time."),
                                        new CityIntentMetric("Mileage pressure", String.format("%.1f mi/hr",
                                                        scenario.getMiles() / (double) scenario.getHours()),
                                                        "High miles per hour means the best area must keep returns short."));
                        case APP_COMPARISON -> {
                                CityScenario uberScenario = generateScenarioByWorkLevel(city, "uber", WorkLevel.SIDE_HUSTLE);
                                CityScenario doordashScenario = generateScenarioByWorkLevel(city, "doordash",
                                                WorkLevel.SIDE_HUSTLE);
                                double gap = doordashScenario.getNetHourly() - uberScenario.getNetHourly();
                                yield List.of(
                                                new CityIntentMetric("DoorDash net hourly", String.format("$%.2f/hr",
                                                                doordashScenario.getNetHourly()),
                                                                "DoorDash side-hustle baseline after mileage and tax assumptions."),
                                                new CityIntentMetric("Uber model net hourly", String.format("$%.2f/hr",
                                                                uberScenario.getNetHourly()),
                                                                "Uber city baseline used as the closest comparable app model."),
                                                new CityIntentMetric("Modeled gap", String.format("%s$%.2f/hr",
                                                                gap >= 0 ? "+" : "-",
                                                                Math.abs(gap)),
                                                                "Positive means DoorDash leads on this modeled side-hustle baseline."));
                        }
                        case MONTHLY_1000 -> List.of(
                                        new CityIntentMetric("Weekly net target", String.format("$%.0f",
                                                        weeklyTargetForThousand),
                                                        "$1,000/month divided across 4.33 average weeks."),
                                        new CityIntentMetric("Hours per week", String.format("%.1f hrs",
                                                        hoursToThousandMonthly),
                                                        "Hours needed at the current city net hourly baseline."),
                                        new CityIntentMetric("Miles per week", String.format("%d mi",
                                                        milesForHours(scenario, hoursToThousandMonthly)),
                                                        "Modeled weekly mileage required for the monthly target."));
                        case NIGHTS_WEEKENDS -> List.of(
                                        new CityIntentMetric("12-hour weekend net", String.format("$%.0f",
                                                        scenario.getNetHourly() * weekendHours),
                                                        "Modeled net profit if weekend windows match the city baseline."),
                                        new CityIntentMetric("Weekend miles", String.format("%d mi",
                                                        milesForHours(scenario, weekendHours)),
                                                        "Estimated mileage load for a 12-hour nights/weekends block."),
                                        new CityIntentMetric("Gross per weekend hour", String.format("$%.2f/hr",
                                                        scenario.getGrossWeekly() / (double) scenario.getHours()),
                                                        "Gross hourly baseline before expense and tax assumptions."));
                };
        }

        private String buildCityIntentJsonLd(
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario,
                        String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "City Earnings Reports",
                                                AppConstants.BASE_URL + "/salary/directory"),
                                buildBreadcrumbItem(3,
                                                String.format("%s %s Driver Earnings", city.getCityName(), appName),
                                                String.format("%s/salary/%s/%s",
                                                                AppConstants.BASE_URL,
                                                                appName.toLowerCase(java.util.Locale.US),
                                                                city.getSlug())),
                                buildBreadcrumbItem(4, intentPage.getDisplayName(), canonicalUrl)));

                Map<String, Object> article = new LinkedHashMap<>();
                article.put("@type", "Article");
                article.put("headline", buildCityIntentTitle(appName, city, intentPage, scenario));
                article.put("url", canonicalUrl);
                article.put("description", String.format(
                                "%s %s %s estimate with net hourly pay, mileage assumptions, driver field notes, and calculator links.",
                                appName,
                                city.getCityName(),
                                intentPage.getSearchPhrase()));
                article.put("isAccessibleForFree", true);

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, article));
                return toJsonLd(graph);
        }

        private String chooseNonBlank(String primary, String fallback) {
                if (primary != null && !primary.isBlank()) {
                        return primary;
                }
                return fallback;
        }

        private List<DriverFieldNote> buildDriverFieldNotes(
                        String app,
                        String appName,
                        CityData city,
                        CityScenario featuredScenario) {
                List<DriverFieldNote> notes = new ArrayList<>();

                if ("doordash".equals(app)) {
                        notes.add(new DriverFieldNote(
                                        "Field test",
                                        "Gross payout is not the number drivers keep",
                                        String.format(
                                                        "A recent DoorDash field test logged 6.5 hours and 90 miles for $86 gross before fuel and vehicle costs. Read the $%.2f/hr %s estimate here as a net baseline, not as an app-payout screenshot.",
                                                        featuredScenario.getNetHourly(),
                                                        city.getCityName()),
                                        "NerdWallet DoorDash pay test",
                                        "https://www.nerdwallet.com/finance/learn/how-much-does-doordash-pay"));
                        notes.add(new DriverFieldNote(
                                        "Pay mode",
                                        "Earn-by-time still depends on active delivery time",
                                        "DoorDash says Dasher pay combines base pay, tips, and promotions. Drivers still need to separate active delivery time from total time logged in the zone before judging an hourly result.",
                                        "DoorDash Earn by Time help",
                                        "https://help.doordash.com/en-us/dashers/article/time-earnings-mode"));
                } else {
                        notes.add(new DriverFieldNote(
                                        "Field test",
                                        "Miles can overwhelm a strong Uber fare day",
                                        String.format(
                                                        "A recent Uber field test reported about 10 active hours, 10 trips, and 305 miles. For %s, the mileage load is why this page starts with $%.2f/hr net instead of gross fare totals.",
                                                        city.getCityName(),
                                                        featuredScenario.getNetHourly()),
                                        "NerdWallet Uber pay test",
                                        "https://www.nerdwallet.com/finance/learn/how-much-does-an-uber-driver-make"));
                        notes.add(new DriverFieldNote(
                                        "Online time",
                                        "Drivers judge waiting time, not only booked trips",
                                        "Uber's earnings tools show session summaries with online time, offers, and completed trips. That is the right mental model for comparing a city: unpaid waiting and repositioning time change the real hourly rate.",
                                        "Uber delivery earnings guide",
                                        "https://www.uber.com/deliver/earnings/"));
                }

                addCitySpecificDriverFieldNotes(notes, app, city);

                notes.add(new DriverFieldNote(
                                "Mileage floor",
                                "A low dollar-per-mile offer can erase the shift",
                                String.format(
                                                "Driver threads often use a dollar-per-mile floor before accepting work. The 2026 IRS business mileage rate is $0.725/mi, so %s drivers should compare every offer against miles, not only dollars.",
                                                city.getCityName()),
                                "IRS 2026 mileage rate",
                                "https://www.irs.gov/forms-pubs/the-standard-mileage-rates-and-maximum-automobile-fair-market-values-have-been-updated-for-2026"));

                notes.add(new DriverFieldNote(
                                "Market timing",
                                "Peak windows beat all-day availability",
                                "Recent driver discussions keep repeating the same pattern: good markets can work at peak windows, while summer, school breaks, slow months, and driver saturation make all-day grinding much less reliable.",
                                "Driver discussion on 2026 demand",
                                "https://www.reddit.com/r/doordash_drivers/comments/1tydq5v/is_doordash_still_worth_it_in_2026/"));

                notes.add(new DriverFieldNote(
                                "Platform spread",
                                "Customer prices can rise while driver pay barely moves",
                                "Gridwise reported customer rideshare prices up 9.6% and platform fees up 33.2%, while driver gross pay per hour rose 4.1%. That gap is why net earnings pages need cost and time assumptions.",
                                "Gridwise 2026 gig mobility report",
                                "https://www.prnewswire.com/news-releases/gridwise-analytics-annual-gig-mobility-report-finds-customer-rideshare-prices-rose-nearly-10-as-platform-fees-surged-and-driver-pay-lagged-302704761.html"));

                return notes;
        }

        private void addCitySpecificDriverFieldNotes(List<DriverFieldNote> notes, String app, CityData city) {
                String citySlug = city.getSlug();
                if ("uber".equals(app)) {
                        switch (citySlug) {
                                case "chicago" -> notes.add(new DriverFieldNote(
                                                "Chicago pattern",
                                                "Airport and bar-close windows beat generic 12-hour days",
                                                "Chicago driver discussions point to early airport business trips and late weekend bar-close demand as stronger lanes, but also call out rapid vehicle mileage accumulation. Treat high gross days as conditional on timing and car replacement costs.",
                                                "AskChicago Uber/Lyft summer thread",
                                                "https://www.reddit.com/r/AskChicago/comments/1rep6pm/do_chicago_lyftuber_drivers_make_good_money_in/"));
                                case "los-angeles" -> notes.add(new DriverFieldNote(
                                                "Los Angeles pattern",
                                                "Night shifts and low fuel cost change the LA math",
                                                "Los Angeles driver reports often separate graveyard/airport strategy from daytime traffic. A hybrid or EV can make the same gross payout look better, while daytime congestion can push net hourly far below the headline number.",
                                                "LA driver weekly earnings discussion",
                                                "https://www.reddit.com/r/lyftdrivers/comments/1pl9p05/who_is_actually_still_making_1k_or_more_a_week/"));
                                case "austin" -> notes.add(new DriverFieldNote(
                                                "Austin pattern",
                                                "Selective acceptance matters more than the city average",
                                                "Austin driver discussion shows a wide gap between high-performing selective shifts and monthly averages after gas, mileage, insurance, and weak weeks. Treat strong weekend claims as a strategy signal, not the default baseline.",
                                                "Austin Uber earnings discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/1rlrq6h/1750_a_week_in_austin/"));
                                case "seattle" -> notes.add(new DriverFieldNote(
                                                "Seattle pattern",
                                                "Saturation makes weekend testing more useful than old weekly claims",
                                                "Seattle-area driver discussion points to lower weekly pay than prior years, more driver supply, and advice to test Friday/Saturday evening windows before assuming the market still supports old full-time numbers.",
                                                "Seattle Uber weekly pay discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/1ok6xj2/seattle_area_drivers_whats_your_average_weekly_pay/"));
                                case "las-vegas" -> notes.add(new DriverFieldNote(
                                                "Las Vegas pattern",
                                                "Strip and event traffic can make active-hour screenshots misleading",
                                                "Las Vegas driver discussion around the Strip and EDC-style event weekends shows why active-hour earnings need a traffic and dead-time check. High gross event windows can still depend on avoiding rides that burn too much time.",
                                                "Las Vegas Uber driver event discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/1tgdvff/vegas_drivers_tell_me_how_your_edc_weekend_has/"));
                                case "orlando" -> notes.add(new DriverFieldNote(
                                                "Orlando pattern",
                                                "Weekend tourism demand needs a fuel and hour check",
                                                "Orlando driver discussion reports strong Friday-Sunday gross totals, but the same thread immediately raises fuel cost. That is why the Orlando estimate should be checked against real weekend hours and mileage.",
                                                "Orlando Uber weekend earnings discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/somguu/how_much_are_uber_drivers_making_in_orlando_with/"));
                                case "nashville" -> notes.add(new DriverFieldNote(
                                                "Nashville pattern",
                                                "Supply and event timing matter more than the city average",
                                                "Recent Nashville driver discussion frames the market as sensitive to out-of-state driver supply, weak regulation, and app transparency differences. For this page, the city average should be treated as a starting point, not a promise for random weekday hours.",
                                                "Nashville Uber/Lyft market discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/1qwnmas/hows_the_nashville_uberlyft_market_right_now/"));
                                case "new-orleans" -> notes.add(new DriverFieldNote(
                                                "New Orleans pattern",
                                                "Vehicle tier and season can change the loop",
                                                "New Orleans driver discussion separates stronger XL/surge windows from lower-value X loops that add vehicle miles. Read the city estimate as an all-in baseline before assuming tourist demand alone makes every ride profitable.",
                                                "New Orleans Uber tier discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/9n86pd/do_you_make_more_money_driving_for_uber_xl_as/"));
                                case "portland" -> notes.add(new DriverFieldNote(
                                                "Portland pattern",
                                                "Local policy debate is a pay signal",
                                                "Portland is actively debating ride-hail driver pay and platform take rates. That matters for earnings pages because rider cost and driver take-home can move in different directions even when trip demand looks stable.",
                                                "NW Labor Press Portland pay-cap report",
                                                "https://nwlaborpress.org/2026/06/portland-may-look-at-capping-uber-lyfts-grab-of-driver-earnings/"));
                                default -> {
                                }
                        }
                } else if ("doordash".equals(app)) {
                        switch (citySlug) {
                                case "austin" -> notes.add(new DriverFieldNote(
                                                "Austin pattern",
                                                "Zone choice changes the DoorDash math",
                                                "Austin dasher discussion separates Central Austin density, South Austin demand, and West Austin wait-time/mileage drag. This is why a city average needs a zone check before a driver trusts the estimate.",
                                                "Austin DoorDash zone discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/1lspql5/austin_dashing/"));
                                case "denver" -> notes.add(new DriverFieldNote(
                                                "Denver pattern",
                                                "Centennial and suburb runs can pull the average down",
                                                "Denver-area dasher discussion specifically calls out a drop from stronger 2024 hourly results to roughly $12-$15/hr in Centennial-style suburban work. That is why the Denver estimate needs a mileage and zone check before a driver trusts it.",
                                                "Denver DoorDash driver discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/1j4uofw/is_anybody_dashing_around_denver_area_if_yes_how/"));
                                case "phoenix" -> notes.add(new DriverFieldNote(
                                                "Phoenix pattern",
                                                "$100 days can disappear when the market slows",
                                                "Phoenix-area dasher discussion specifically frames the pain as not being able to make $100/day when order volume slows. For Phoenix, the $100/day page should be treated as a hours-and-miles plan, not a routine expectation.",
                                                "Phoenix DoorDash slowdown discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/nb6oi7/doordash_been_slow_lately_its_been_hard_to_get/"));
                                case "san-jose" -> notes.add(new DriverFieldNote(
                                                "San Jose pattern",
                                                "Prop 22 changes active-time math, not total waiting time",
                                                "San Jose/California driver discussion shows confusion around minimum wage, active miles, and Prop 22 calculations. California pages need active-time and waiting-time checks instead of a simple gross screenshot.",
                                                "San Jose Prop 22 driver discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/p5m8is/minimum_wage_ca/"));
                                case "minneapolis" -> notes.add(new DriverFieldNote(
                                                "Minneapolis pattern",
                                                "Suburb strategy can beat downtown parking friction",
                                                "Minneapolis/St. Paul dasher discussion points to stronger results in surrounding areas such as Saint Paul, Woodbury, White Bear, and Eagan, while warning that parking and dense-city friction can hurt downtown runs.",
                                                "Minnesota DoorDash market discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/14u1c5e/does_anyone_dash_in_minnesota_i_moved_from/"));
                                case "atlanta" -> notes.add(new DriverFieldNote(
                                                "Georgia pattern",
                                                "Active time can hide the real dash-time rate",
                                                "A Georgia dasher earnings post showed $560.31 across 74 deliveries with 19h 24m active time and 23h 39m dash time. Atlanta drivers should compare both clocks before deciding whether a shift really clears the local net target.",
                                                "Georgia DoorDash active-time post",
                                                "https://www.reddit.com/r/DoorDashDrivers/comments/1r0oqfo/560_week_in_ga_not_atlanta_74_del_19_active_hrs/"));
                                case "dallas" -> notes.add(new DriverFieldNote(
                                                "Dallas pattern",
                                                "DFW zone crossings make home-to-home miles matter",
                                                "Dallas/DFW delivery discussions repeatedly come back to dollar-per-mile discipline and zone crossing. In a spread-out metro, count miles from home to home, not only the restaurant-to-dropoff route.",
                                                "DoorDash dollar-per-mile discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/1s39uhj/whats_your_minimum_mile_to_accept_doordash_orders/"));
                                default -> {
                                }
                        }
                }
        }

        private Map<String, List<CityData>> buildCoverageByRegion(List<CityData> coveredCities) {
                Map<String, List<CityData>> coverageByRegion = new LinkedHashMap<>();
                coverageByRegion.put("West", filterCitiesByRegion(coveredCities, "West"));
                coverageByRegion.put("South", filterCitiesByRegion(coveredCities, "South"));
                coverageByRegion.put("Midwest", filterCitiesByRegion(coveredCities, "Midwest"));
                coverageByRegion.put("Northeast", filterCitiesByRegion(coveredCities, "Northeast"));
                return coverageByRegion;
        }

        private List<CityData> filterCitiesByRegion(List<CityData> coveredCities, String region) {
                return coveredCities.stream()
                                .filter(city -> region.equals(regionForState(city.getState())))
                                .collect(Collectors.toList());
        }

        private String regionForState(String state) {
                return switch (state) {
                        case "CA", "WA", "OR", "NV", "AZ", "CO", "NM", "HI" -> "West";
                        case "TX", "FL", "GA", "NC", "TN", "KY", "LA", "VA", "DC", "MD", "OK" -> "South";
                        case "IL", "OH", "IN", "MI", "WI", "MN", "MO", "KS", "NE" -> "Midwest";
                        default -> "Northeast";
                };
        }

        private boolean requiresEditorialReview(String contentType) {
                if (contentType == null) {
                        return true;
                }
                return "user_submitted".equalsIgnoreCase(contentType.trim());
        }

        private String buildBestCitiesItemListJsonLd(String appName, String app, List<CityRankingDto> rankedCities) {
                List<Map<String, Object>> itemListElements = new ArrayList<>();
                int limit = Math.min(rankedCities.size(), 20);
                for (int i = 0; i < limit; i++) {
                        CityRankingDto ranking = rankedCities.get(i);
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("@type", "ListItem");
                        item.put("position", i + 1);
                        item.put("name", ranking.city().getCityName());
                        item.put("url", String.format("%s/salary/%s/%s",
                                        AppConstants.BASE_URL,
                                        app,
                                        ranking.city().getSlug()));
                        itemListElements.add(item);
                }

                Map<String, Object> itemList = new LinkedHashMap<>();
                itemList.put("@context", "https://schema.org");
                itemList.put("@type", "ItemList");
                itemList.put("name", String.format("%s driver earnings ranking by city in %d", appName,
                                java.time.LocalDate.now().getYear()));
                itemList.put("description",
                                String.format(
                                                "Ranking of US cities based on estimated %s driver earnings after mileage and self-employment tax assumptions.",
                                                appName));
                itemList.put("itemListElement", itemListElements);
                return toJsonLd(itemList);
        }

        private String buildBestCitiesFaqJsonLd(String appName, String app, List<CityRankingDto> rankedCities,
                        int currentYear) {
                CityRankingDto topCity = rankedCities.get(0);
                int rankedCount = rankedCities.size();
                String q1 = String.format("What is the highest-paying city for %s drivers in %d?",
                                appName,
                                currentYear);
                String a1 = String.format(
                                "In GigVerdict's current after-expenses earnings ranking, %s leads at about $%.2f per hour net after mileage and self-employment tax assumptions.",
                                topCity.city().getCityName(),
                                topCity.netHourly());
                String q2 = String.format("Is this a coverage list or an earnings ranking for %s?", appName);
                String a2 = app.equals("uber")
                                ? "This page is an after-expenses earnings ranking. Use the Uber coverage guide and Uber's official city directory when your question is whether a market is active."
                                : "This page is an after-expenses earnings ranking. Use the DoorDash availability guide and DoorDash's Dasher signup flow when your question is whether you can dash in a market.";
                String q3 = String.format("How many %s city earnings pages are ranked here?", appName);
                String a3 = String.format(
                                "GigVerdict currently ranks %d U.S. city earnings pages for %s and links each market to a deeper page with an adjustable calculator.",
                                rankedCount,
                                appName);

                List<Map<String, Object>> mainEntity = List.of(
                                buildFaqQuestion(q1, a1),
                                buildFaqQuestion(q2, a2),
                                buildFaqQuestion(q3, a3));

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@context", "https://schema.org");
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", mainEntity);
                return toJsonLd(faqPage);
        }

        private String buildAppHubSchemaGraph(String appName, String app, List<CityRankingDto> topCities,
                        long indexedCityCount) {
                List<Map<String, Object>> breadcrumbItems = new ArrayList<>();
                breadcrumbItems.add(buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"));
                breadcrumbItems.add(buildBreadcrumbItem(2, "City Earnings Reports", AppConstants.BASE_URL + "/salary/directory"));
                breadcrumbItems.add(buildBreadcrumbItem(3,
                                String.format("%s Driver Earnings by City", appName),
                                String.format("%s/salary/%s", AppConstants.BASE_URL, app)));

                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", breadcrumbItems);

                List<Map<String, Object>> itemListElements = new ArrayList<>();
                int limit = Math.min(topCities.size(), 10);
                for (int i = 0; i < limit; i++) {
                        CityRankingDto ranking = topCities.get(i);
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("@type", "ListItem");
                        item.put("position", i + 1);
                        item.put("name", ranking.city().getCityName());
                        item.put("url", String.format("%s/salary/%s/%s",
                                        AppConstants.BASE_URL,
                                        app,
                                        ranking.city().getSlug()));
                        itemListElements.add(item);
                }

                Map<String, Object> itemList = new LinkedHashMap<>();
                itemList.put("@type", "ItemList");
                itemList.put("name", String.format("%s driver earnings by city", appName));
                itemList.put("itemListOrder", "https://schema.org/ItemListOrderDescending");
                itemList.put("numberOfItems", itemListElements.size());
                itemList.put("itemListElement", itemListElements);

                CityRankingDto topCity = topCities.get(0);
                String q1 = String.format("How do %s driver earnings compare by city in 2026?", appName);
                String a1 = String.format(
                                "GigVerdict currently tracks %d %s city earnings pages. In the current side-hustle ranking, %s leads at about $%.2f per hour net after mileage and self-employment tax assumptions. Open any city page to compare part-time, side-hustle, and full-time scenarios.",
                                indexedCityCount,
                                appName,
                                topCity.city().getCityName(),
                                topCity.netHourly());

                String q2 = String.format("Is this an official %s coverage list?", appName);
                String a2 = app.equals("uber")
                                ? "No. This hub compares estimated driver earnings by city. Use the separate Uber coverage guide and Uber's official city directory to confirm that a market is active before assuming coverage."
                                : "No. This hub compares estimated driver earnings by city and is not an official coverage directory. Check DoorDash's own onboarding flow or local app availability to confirm that a market is active.";

                String q3 = String.format("What does each %s city earnings page include?", appName);
                String a3 = "Each city earnings page includes part-time, side-hustle, and full-time earnings scenarios, mileage-based cost assumptions, quarterly tax context, and links to the app calculator so you can adjust the numbers for your own routine.";

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", List.of(
                                buildFaqQuestion(q1, a1),
                                buildFaqQuestion(q2, a2),
                                buildFaqQuestion(q3, a3)));

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, itemList, faqPage));
                return toJsonLd(graph);
        }

        private String buildCityFaqJsonLd(String appName, CityData city, CityScenario featuredScenario) {
                String q1 = String.format("How much do %s drivers make in %s after expenses in 2026?",
                                appName,
                                city.getCityName());
                String a1 = String.format(
                                "Based on our estimates for %s, a side-hustle %s driver (25 hrs/week) earns approximately $%.2f per hour after deducting gas ($%.2f/gal), vehicle depreciation (IRS rate: $0.725/mile), and 15.3%% self-employment tax. This translates to roughly $%.0f in net weekly profit.",
                                city.getCityName(),
                                appName,
                                featuredScenario.getNetHourly(),
                                city.getGasPrice(),
                                featuredScenario.getNetProfit());

                String q2 = String.format("Is %s worth it in %s in 2026?",
                                appName,
                                city.getCityName());
                String viability = featuredScenario.getNetHourly() >= 15.0
                                ? "This is above the federal minimum wage, making it potentially viable as supplemental income."
                                : "This is close to or below minimum wage in many states, meaning a traditional W-2 job may offer better compensation plus benefits.";
                String a2 = String.format(
                                "It depends on your work level and vehicle. At a side-hustle pace in %s, the estimated net hourly wage is $%.2f/hr. %s Vehicle choice matters: a Toyota Prius (57 MPG) often yields higher net pay than a Ford Explorer (23 MPG).",
                                city.getCityName(),
                                featuredScenario.getNetHourly(),
                                viability);

                String q3 = String.format("How are these %s driver earnings calculated?", appName);
                String a3 = String.format(
                                "We estimate net profit by starting with gross income levels typical for %s markets, then subtracting fuel costs ($%.2f/gal local average), vehicle depreciation (using the 2026 IRS standard mileage rate of $0.725/mile), and estimated self-employment taxes (15.3%%). The result is your estimated take-home pay per hour.",
                                city.getMarketTier(),
                                city.getGasPrice());

                String q4 = String.format("What is the best car for %s driving in %s?", appName, city.getCityName());
                String a4 = city.isHighTraffic()
                                ? String.format(
                                                "In %s traffic, a hybrid such as the Toyota Prius (57 MPG) or Corolla Hybrid (52 MPG) is ideal because city driving improves hybrid efficiency. Gas-only SUVs can drop to 15-18 MPG in congestion and reduce margins.",
                                                city.getCityName())
                                : String.format(
                                                "For %s driving conditions, a Toyota Prius (57 MPG) or Camry Hybrid (51 MPG) offers strong cost-per-mile performance. If SUV capacity is required for XL rides, the RAV4 Hybrid (39 MPG) is typically the most efficient option.",
                                                city.getCityName());

                String q4b = String.format("What should I check before trusting this %s earnings estimate?", appName);
                String a4b = String.format(
                                "Check total miles, active time versus online or dash time, dollar-per-mile floor, tips, and whether your shift matches the strongest local windows. The driver field notes on this page show why the $%.2f/hr net estimate should be treated as a planning baseline, not a guaranteed payout.",
                                featuredScenario.getNetHourly());

                String q5 = String.format("How much should a %s driver in %s save for taxes?",
                                appName,
                                city.getCityName());
                double quarterlyTax = Math.max(0,
                                (featuredScenario.getGrossWeekly() - (featuredScenario.getMiles() * 0.725)) * 0.153)
                                * 13;
                String a5 = String.format(
                                "As an independent contractor (1099), you owe approximately 15.3%% self-employment tax on net profit, plus federal and state income tax. For a side-hustle driver in %s grossing $%d/week, estimated quarterly self-employment tax is about $%.0f. A common rule is to set aside 25-30%% of net profit for taxes and pay quarterly.",
                                city.getCityName(),
                                featuredScenario.getGrossWeekly(),
                                quarterlyTax);

                String q6 = String.format("Why do %s driver earnings in %s differ from other cities?",
                                appName,
                                city.getCityName());
                String trafficDescriptor = city.isHighTraffic()
                                ? "heavy traffic congestion that increases hours per delivery"
                                : "moderate traffic conditions";
                String a6 = String.format(
                                "Calculator estimates vary due to local factors. %s has gas at $%.2f/gal, %s, and is classified as a %s market. High-cost cities can have higher gross pay and higher expenses, while lower-cost cities may provide better effective margins.",
                                city.getCityName(),
                                city.getGasPrice(),
                                trafficDescriptor,
                                city.getMarketTier());

                String q7 = String.format("Is it better to drive for Uber or DoorDash in %s?",
                                city.getCityName());
                String channelComparison = city.isHighTraffic()
                                ? String.format(
                                                "In high-traffic areas like %s, DoorDash deliveries can involve more parking challenges but shorter distances, while Uber rides can get delayed in congestion with passengers.",
                                                city.getCityName())
                                : String.format(
                                                "In %s, Uber can have higher per-trip gross pay for rideshare while DoorDash can offer more flexible food-delivery windows.",
                                                city.getCityName());
                String a7 = String.format(
                                "Both platforms operate in the %s %s market with similar expense pressure. %s Many top earners multi-app by running both platforms to minimize downtime. Use the comparison tool to evaluate the current market tradeoff.",
                                city.getCityName(),
                                city.getMarketTier(),
                                channelComparison);

                List<Map<String, Object>> mainEntity = List.of(
                                buildFaqQuestion(q1, a1),
                                buildFaqQuestion(q2, a2),
                                buildFaqQuestion(q3, a3),
                                buildFaqQuestion(q4, a4),
                                buildFaqQuestion(q4b, a4b),
                                buildFaqQuestion(q5, a5),
                                buildFaqQuestion(q6, a6),
                                buildFaqQuestion(q7, a7));

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@context", "https://schema.org");
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", mainEntity);
                return toJsonLd(faqPage);
        }

        private String buildCoverageFaqJsonLd(String appName, int coveredCityCount, String officialCoverageAnswer) {
                String q1 = String.format("Is this an official %s coverage list?", appName);
                String a1 = "No. This page is a navigation guide. " + officialCoverageAnswer;

                String q2 = String.format("How many %s city earnings pages does GigVerdict cover right now?", appName);
                String a2 = String.format(
                                "GigVerdict currently links %d covered city earnings pages for %s. Each page focuses on net hourly earnings after mileage, fuel, and self-employment tax assumptions.",
                                coveredCityCount,
                                appName);

                String q3 = String.format("What should I do after I confirm my %s city is active?", appName);
                String a3 = String.format(
                                "Open the matching GigVerdict city earnings page to compare estimated take-home pay, then review the best-cities ranking if you are deciding between markets or planning a move for %s work.",
                                appName);

                List<Map<String, Object>> mainEntity = List.of(
                                buildFaqQuestion(q1, a1),
                                buildFaqQuestion(q2, a2),
                                buildFaqQuestion(q3, a3));

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@context", "https://schema.org");
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", mainEntity);
                return toJsonLd(faqPage);
        }

        private String renderCoveragePage(String app,
                        String appName,
                        String coverageVerb,
                        String canonicalPath,
                        String officialSourceName,
                        String officialSourceSummary,
                        String officialSourceUrl,
                        String officialSourceCtaLabel,
                        String officialCoverageFaqAnswer,
                        Model model) {
                List<CityData> coveredCities = Arrays.stream(CityData.values())
                                .filter(city -> pageIndexPolicyService.isCityReportIndexable(city, app))
                                .sorted((left, right) -> left.getCityName().compareTo(right.getCityName()))
                                .collect(Collectors.toList());

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                String title = String.format("Where You Can %s for %s in the US (%s)", coverageVerb, appName, monthYear);
                String description = String.format(
                                "Verify current %s availability, then compare %d city earnings pages for estimated net hourly pay after expenses. Updated %s.",
                                appName,
                                coveredCities.size(),
                                monthYear);
                String canonicalUrl = String.format("%s%s", AppConstants.BASE_URL, canonicalPath);

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("coverageVerb", coverageVerb);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("coveredCityCount", coveredCities.size());
                model.addAttribute("coverageByRegion", buildCoverageByRegion(coveredCities));
                model.addAttribute("officialSourceName", officialSourceName);
                model.addAttribute("officialSourceSummary", officialSourceSummary);
                model.addAttribute("officialSourceUrl", officialSourceUrl);
                model.addAttribute("officialSourceCtaLabel", officialSourceCtaLabel);
                model.addAttribute("coverageFaqJsonLd",
                                buildCoverageFaqJsonLd(appName, coveredCities.size(), officialCoverageFaqAnswer));
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "salary/app-coverage";
        }

        private List<CityEarningsSnapshot> buildUberHourlyEarningsSnapshots() {
                return List.of(
                                buildUberHourlyEarningsSnapshot(
                                                "atlanta",
                                                "uber driver hourly earnings atlanta ga 2025 2026",
                                                "rideshare driver hourly earnings atlanta ga 2025 2026",
                                                "Atlanta has two hourly-earnings variants with enough demand to justify a dedicated city check."),
                                buildUberHourlyEarningsSnapshot(
                                                "los-angeles",
                                                "uber driver earnings los angeles 2025 2026",
                                                "rideshare driver hourly earnings los angeles ca 2025 2026",
                                                "Los Angeles needs a traffic and mileage explanation because broad earnings claims can hide long online time."),
                                buildUberHourlyEarningsSnapshot(
                                                "austin",
                                                "uber driver hourly earnings austin tx 2025 2026",
                                                "rideshare driver hourly earnings austin tx 2025 2026",
                                                "Austin is useful for selective-shift searches where drivers compare weekend upside against weak weeks."),
                                buildUberHourlyEarningsSnapshot(
                                                "chicago",
                                                "uber driver hourly earnings chicago il 2025 2026",
                                                "rideshare driver hourly earnings chicago il 2025 2026 uber lyft",
                                                "Chicago connects hourly earnings intent with airport, business-trip, and bar-close timing questions."),
                                buildUberHourlyEarningsSnapshot(
                                                "houston",
                                                "uber driver hourly earnings houston tx 2025 2026",
                                                "rideshare driver hourly earnings houston tx 2025 2026",
                                                "Houston needs a spread-out-market explanation because home-to-home miles can pull down hourly pay."),
                                buildUberHourlyEarningsSnapshot(
                                                "orlando",
                                                "uber driver earnings orlando florida 2026",
                                                "rideshare driver hourly earnings orlando florida 2026",
                                                "Orlando is the tourism-demand test case: weekend gross can look strong until fuel and online time are counted."));
        }

        private List<DoorDashDurationEstimate> buildDoorDashDurationEstimates() {
                return List.of(
                                new DoorDashDurationEstimate(
                                                "3-hours",
                                                "3 Hours",
                                                "how much can you make with DoorDash in 3 hours",
                                                3.0,
                                                60,
                                                90,
                                                90,
                                                130,
                                                35,
                                                60,
                                                "Friday or Saturday dinner, roughly 5 p.m. to 8 p.m.",
                                                "Mid-afternoon gaps after lunch and before dinner.",
                                                "A 3-hour DoorDash shift works only when the whole block sits inside a peak window; otherwise the first unpaid wait can wreck the hourly result."),
                                new DoorDashDurationEstimate(
                                                "4-hours",
                                                "4 Hours",
                                                "how much can you make with DoorDash in 4 hours",
                                                4.0,
                                                70,
                                                105,
                                                90,
                                                140,
                                                45,
                                                70,
                                                "Dinner plus one shoulder hour, roughly 5 p.m. to 9 p.m.",
                                                "Late morning or late night without nearby restaurant density.",
                                                "The fourth hour usually earns less than the peak core, so the best 4-hour plan starts before dinner demand fully spikes."),
                                new DoorDashDurationEstimate(
                                                "6-hours",
                                                "6 Hours",
                                                "how much can you make with DoorDash in 6 hours",
                                                6.0,
                                                115,
                                                170,
                                                180,
                                                220,
                                                75,
                                                115,
                                                "Lunch plus dinner, with a break between slow periods.",
                                                "One continuous off-peak block that forces waiting.",
                                                "A 6-hour DoorDash shift needs two good windows or one strong market; otherwise the slow middle hours pull down net hourly pay."),
                                new DoorDashDurationEstimate(
                                                "8-hours",
                                                "8 Hours",
                                                "how much can you make with DoorDash in 8 hours",
                                                8.0,
                                                140,
                                                220,
                                                210,
                                                280,
                                                90,
                                                150,
                                                "Lunch, dinner, and late weekend demand with planned downtime.",
                                                "All-day grinding through weak order volume.",
                                                "Eight hours can produce a bigger gross number, but it usually lowers hourly efficiency unless the driver avoids dead miles and slow gaps."),
                                new DoorDashDurationEstimate(
                                                "a-day",
                                                "a Day",
                                                "how much can you make with DoorDash in a day",
                                                9.0,
                                                150,
                                                250,
                                                220,
                                                320,
                                                95,
                                                165,
                                                "Lunch plus dinner, with a real break before the dinner peak.",
                                                "Starting too early and staying online through long idle windows.",
                                                "A full DoorDash day is less about being online all day and more about stacking lunch, dinner, and late demand while cutting off weak hours."),
                                new DoorDashDurationEstimate(
                                                "a-week",
                                                "a Week",
                                                "how much can you make with DoorDash in a week",
                                                20.0,
                                                300,
                                                500,
                                                700,
                                                1100,
                                                190,
                                                340,
                                                "15-20 peak-focused hours across Friday, Saturday, Sunday, and two dinner shifts.",
                                                "Spreading the same hours across random weekdays.",
                                                "Weekly DoorDash earnings depend more on shift selection than raw availability; concentrated peak hours usually beat more total hours in weak windows."));
        }

        private List<DoorDashMoneyIntent> buildDoorDashMoneyIntents() {
                return List.of(
                                new DoorDashMoneyIntent(
                                                "can-you-make-100-a-day",
                                                "$100 a Day",
                                                "can you make $100 a day with DoorDash",
                                                "Can You Make $100 a Day with DoorDash?",
                                                "$100 net is realistic in many markets, but it usually requires a planned peak-window shift rather than random all-day availability.",
                                                "Typical net target",
                                                "$100",
                                                "Modeled time",
                                                "5.5-8.5 hrs",
                                                145,
                                                175,
                                                100,
                                                115,
                                                5.5,
                                                8.5,
                                                65,
                                                110,
                                                "Dinner plus one shoulder hour, or lunch plus dinner in a dense zone.",
                                                "Slow weekday gaps, long pickups, and unpaid return miles can turn $100 gross into a weak net day.",
                                                "Start with a $1.50-$2.00 per mile floor, stop accepting orders that pull you away from restaurants, and measure the result after gas and mileage."),
                                new DoorDashMoneyIntent(
                                                "can-you-make-200-a-day",
                                                "$200 a Day",
                                                "can you make $200 a day with DoorDash",
                                                "Can You Make $200 a Day with DoorDash?",
                                                "$200 net is an aggressive target; the page treats it as a long two-peak plan, not a normal short shift.",
                                                "Typical net target",
                                                "$200",
                                                "Modeled time",
                                                "10.0-13.0 hrs",
                                                285,
                                                340,
                                                190,
                                                220,
                                                10.0,
                                                13.0,
                                                130,
                                                210,
                                                "Friday or Saturday with lunch, dinner, and late demand stacked together.",
                                                "A $200 day breaks when the driver grinds low-order hours or accepts high-mileage orders to keep moving.",
                                                "Use $200/day only as a dense-market or weekend stretch goal, then compare the plan against the $100/day and 8-hour pages."),
                                new DoorDashMoneyIntent(
                                                "after-gas",
                                                "After Gas",
                                                "DoorDash after gas",
                                                "DoorDash After Gas: Real Net Pay",
                                                "After gas, mileage, and tax reserves, many DoorDash shifts keep roughly 55%-70% of gross payout as modeled take-home.",
                                                "Typical net",
                                                "$90-$150",
                                                "Gross kept",
                                                "55%-70%",
                                                140,
                                                220,
                                                90,
                                                150,
                                                8.0,
                                                10.0,
                                                80,
                                                145,
                                                "Short pickup loops during lunch and dinner, especially when drop-offs stay near restaurants.",
                                                "Long suburb returns and apartment waits can erase the difference between a good payout and a good net hour.",
                                                "Check gas separately, but do not stop there: compare every shift against mileage cost, tax reserve, and unpaid repositioning time."),
                                new DoorDashMoneyIntent(
                                                "pay-per-mile",
                                                "Pay Per Mile",
                                                "DoorDash pay per mile",
                                                "DoorDash Pay Per Mile: Acceptance Floor",
                                                "DoorDash pay per mile matters because a shift can look fine by the hour while failing after miles, gas, and vehicle cost.",
                                                "Offer floor",
                                                "$1.50-$2.00/mi",
                                                "Modeled net",
                                                "$0.65-$1.10/mi",
                                                140,
                                                220,
                                                90,
                                                150,
                                                8.0,
                                                10.0,
                                                80,
                                                145,
                                                "Dense restaurants where pickups and drop-offs keep the driver inside the same earning zone.",
                                                "Orders that cross zones, require unpaid returns, or barely clear the IRS mileage proxy.",
                                                "Use dollars per mile as the first filter, then confirm the shift still clears the hourly target after waiting time."));
        }

        private List<DoorDashAdjustmentScenario> buildDoorDashAdjustmentScenarios() {
                return List.of(
                                new DoorDashAdjustmentScenario(
                                                "California",
                                                "Prop 22 weekly pay adjustment",
                                                "California Prop 22 uses active time from acceptance to completion plus active miles for qualifying pickups. Tips do not count against the guaranteed earnings floor.",
                                                10.0,
                                                100.0,
                                                190.00,
                                                110.50,
                                                20.28,
                                                0.37,
                                                "DoorDash's California example produces a $239.80 guarantee, a $49.80 pay adjustment, and $350.30 total with tips.",
                                                "DoorDash Prop 22 guide",
                                                "https://help.doordash.com/en-us/dashers/article/california-dashers"),
                                new DoorDashAdjustmentScenario(
                                                "New York City",
                                                "NYC minimum earnings adjustment",
                                                "NYC applies the minimum earnings standard to qualifying active time on orders picked up or dropped off in New York City.",
                                                5.0,
                                                0.0,
                                                70.00,
                                                50.00,
                                                22.13,
                                                0.0,
                                                "DoorDash's NYC example produces a $110.65 minimum, a $40.65 pay adjustment, and $160.65 total with tips.",
                                                "DoorDash NYC earnings standard",
                                                "https://help.doordash.com/en-us/dashers/article/guide-to-the-new-york-city-earnings-standard"),
                                new DoorDashAdjustmentScenario(
                                                "Seattle",
                                                "Seattle adjusted pay",
                                                "Seattle recalculates offer pay from active minutes and active miles, then pays the difference when the recalculated amount is higher than offered pay.",
                                                0.55,
                                                6.3,
                                                18.90,
                                                5.00,
                                                28.20,
                                                0.80,
                                                "DoorDash's Seattle example works out to $20.55 guaranteed DoorDash pay, including a $1.65 adjusted pay difference before tips.",
                                                "DoorDash Seattle regulations",
                                                "https://help.doordash.com/en-us/dashers/article/guide-to-seattle-dasher-regulations"));
        }

        private String buildDoorDashMoneyIntentTitle(DoorDashMoneyIntent intent) {
                return switch (intent.slug()) {
                        case "can-you-make-100-a-day" ->
                                "Can You Make $100 a Day with DoorDash? Real Hours + Miles";
                        case "can-you-make-200-a-day" ->
                                "Can You Make $200 a Day with DoorDash? Hours, Miles, Risk";
                        case "after-gas" ->
                                "DoorDash After Gas: What Drivers Actually Keep";
                        case "pay-per-mile" ->
                                "DoorDash Pay Per Mile: The Offer Floor That Matters";
                        default -> intent.headline() + " 2026";
                };
        }

        private String buildDoorDashAdjustmentPayJsonLd(String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "DoorDash", AppConstants.BASE_URL + "/doordash"),
                                buildBreadcrumbItem(3, "DoorDash Adjustment Pay Calculator", canonicalUrl)));

                Map<String, Object> app = new LinkedHashMap<>();
                app.put("@type", "WebApplication");
                app.put("name", "DoorDash Adjustment Pay Calculator");
                app.put("applicationCategory", "FinanceApplication");
                app.put("operatingSystem", "Any");
                app.put("url", canonicalUrl);
                app.put("description",
                                "Calculator for estimating DoorDash adjustment pay from active time, active miles, DoorDash pay before tips, and local guaranteed earnings rules.");
                app.put("isAccessibleForFree", true);

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", List.of(
                                buildFaqQuestion("What is DoorDash adjustment pay?",
                                                "DoorDash adjustment pay is the difference between a local guaranteed earnings floor and DoorDash pay before tips when the guaranteed amount is higher."),
                                buildFaqQuestion("How do I calculate DoorDash adjustment pay?",
                                                "Estimate the local guarantee from active time and, where the rule includes it, active miles. Then subtract DoorDash pay before tips. If the result is below zero, the adjustment is zero."),
                                buildFaqQuestion("Do tips reduce DoorDash adjustment pay?",
                                                "In the official DoorDash examples for California Prop 22 and NYC, tips are added on top of the adjusted DoorDash pay rather than reducing the guaranteed earnings comparison."),
                                buildFaqQuestion("Where does DoorDash adjustment pay apply?",
                                                "The calculator covers the official examples for California Prop 22, New York City minimum earnings, and Seattle adjusted pay. Other markets may use the normal DoorDash pay model unless a local rule applies.")));

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, app, faqPage));
                return toJsonLd(graph);
        }

        private RedirectView permanentRedirect(String targetUrl) {
                RedirectView redirectView = new RedirectView(targetUrl);
                redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                redirectView.setExposeModelAttributes(false);
                return redirectView;
        }

        private String buildDoorDashShiftEvidenceJsonLd(List<DriverShiftReport> reports, String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "City Earnings Reports",
                                                AppConstants.BASE_URL + "/salary/directory"),
                                buildBreadcrumbItem(3, "DoorDash Driver Shift Evidence 2026", canonicalUrl)));

                List<Map<String, Object>> itemListElements = new ArrayList<>();
                for (int i = 0; i < reports.size(); i++) {
                        DriverShiftReport report = reports.get(i);
                        Map<String, Object> listItem = new LinkedHashMap<>();
                        listItem.put("@type", "ListItem");
                        listItem.put("position", i + 1);
                        listItem.put("name", report.cityName() + " " + report.reportedResult());
                        listItem.put("url", report.sourceUrl());
                        itemListElements.add(listItem);
                }

                Map<String, Object> itemList = new LinkedHashMap<>();
                itemList.put("@type", "ItemList");
                itemList.put("name", "DoorDash driver shift evidence");
                itemList.put("numberOfItems", reports.size());
                itemList.put("itemListElement", itemListElements);

                Map<String, Object> article = new LinkedHashMap<>();
                article.put("@type", "Article");
                article.put("headline", "DoorDash Driver Shift Evidence 2026");
                article.put("url", canonicalUrl);
                article.put("description",
                                "Reviewed DoorDash shift evidence with gross pay, miles, active time, dash time, and estimated net checks.");
                article.put("isAccessibleForFree", true);

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, article, itemList));
                return toJsonLd(graph);
        }

        private String buildDoorDashDurationJsonLd(DoorDashDurationEstimate estimate, String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "DoorDash", AppConstants.BASE_URL + "/doordash"),
                                buildBreadcrumbItem(3, estimate.searchPhrase(), canonicalUrl)));

                Map<String, Object> article = new LinkedHashMap<>();
                article.put("@type", "Article");
                article.put("headline", String.format("How Much Can You Make with DoorDash in %s?",
                                estimate.displayName()));
                article.put("url", canonicalUrl);
                article.put("description", String.format(
                                "%s estimate with gross pay range, net pay range, best shift window, and mileage/tax caveats.",
                                estimate.searchPhrase()));
                article.put("isAccessibleForFree", true);

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", List.of(
                                buildFaqQuestion(
                                                String.format("How much can you make with DoorDash in %s?",
                                                                estimate.displayName().toLowerCase(java.util.Locale.US)),
                                                String.format(
                                                                "A typical DoorDash %s block models about $%d-$%d gross and $%d-$%d net after mileage and tax reserves, depending on market density and shift timing.",
                                                                estimate.displayName().toLowerCase(java.util.Locale.US),
                                                                estimate.grossLow(),
                                                                estimate.grossHigh(),
                                                                estimate.netLow(),
                                                                estimate.netHigh())),
                                buildFaqQuestion("What is the best time to DoorDash for this target?",
                                                estimate.bestWindow()),
                                buildFaqQuestion("What makes this estimate fall apart?",
                                                estimate.weakWindow() + " " + estimate.strategyNote())));

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, article, faqPage));
                return toJsonLd(graph);
        }

        private String buildDoorDashMoneyIntentJsonLd(DoorDashMoneyIntent intent, String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "DoorDash", AppConstants.BASE_URL + "/doordash"),
                                buildBreadcrumbItem(3, intent.displayName(), canonicalUrl)));

                Map<String, Object> article = new LinkedHashMap<>();
                article.put("@type", "Article");
                article.put("headline", intent.headline());
                article.put("url", canonicalUrl);
                article.put("description", String.format(
                                "%s with gross range, net range, hours, miles, best-window guidance, and failure modes.",
                                intent.searchPhrase()));
                article.put("isAccessibleForFree", true);

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", List.of(
                                buildFaqQuestion(intent.headline(),
                                                String.format(
                                                                "%s Typical gross is %s and typical net is %s across about %s and %s.",
                                                                intent.answerLead(),
                                                                intent.grossRange(),
                                                                intent.netRange(),
                                                                intent.hourRange(),
                                                                intent.mileRange())),
                                buildFaqQuestion("What makes this DoorDash estimate fail?",
                                                intent.failureMode()),
                                buildFaqQuestion("What rule should a driver use first?",
                                                intent.decisionRule())));

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, article, faqPage));
                return toJsonLd(graph);
        }

        private CityEarningsSnapshot buildUberHourlyEarningsSnapshot(
                        String citySlug,
                        String primaryQuery,
                        String secondaryQuery,
                        String acquisitionNote) {
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "City not found"));
                CityScenario scenario = generateScenarioByWorkLevel(city, "uber", WorkLevel.SIDE_HUSTLE);
                return new CityEarningsSnapshot(city, scenario, primaryQuery, secondaryQuery, acquisitionNote);
        }

        private List<CityEarningsSnapshot> buildDoorDashHourlyPaySnapshots() {
                return List.of(
                                buildDoorDashHourlyPaySnapshot(
                                                "new-york",
                                                "doordash hourly pay nyc 2026",
                                                "average doordash earnings per hour 2025 2026",
                                                "New York catches broad hourly-pay searches, but the page needs to explain congestion, e-bike economics, and local pay floors instead of quoting a flat national average."),
                                buildDoorDashHourlyPaySnapshot(
                                                "denver",
                                                "doordash driver hourly pay denver 2026",
                                                "doordash denver side hustle earnings after expenses",
                                                "Denver is the field-note anchor: it has city-specific DoorDash evidence and suburb mileage context."),
                                buildDoorDashHourlyPaySnapshot(
                                                "phoenix",
                                                "can you make 100 a day with doordash phoenix",
                                                "doordash phoenix 100 a day earnings 2026",
                                                "Phoenix ties hourly pay to a daily target because heat, spread, and grocery orders change the number of active hours needed."),
                                buildDoorDashHourlyPaySnapshot(
                                                "indianapolis",
                                                "doordash indianapolis side hustle earnings",
                                                "how much can you make with doordash in indianapolis",
                                                "Indianapolis has early side-hustle demand, so the page should explain realistic pay after mileage instead of only gross screenshots."),
                                buildDoorDashHourlyPaySnapshot(
                                                "san-jose",
                                                "doordash driver pay per hour san jose 2026",
                                                "doordash prop 22 active time earnings san jose",
                                                "San Jose needs a California active-time explanation because Prop 22 style searches do not behave like normal hourly-pay searches."),
                                buildDoorDashHourlyPaySnapshot(
                                                "dallas",
                                                "doordash dallas nights and weekends earnings",
                                                "best city to doordash in dallas fort worth",
                                                "Dallas connects city-pay intent with nights/weekends planning and DFW zone-crossing decisions."),
                                buildDoorDashHourlyPaySnapshot(
                                                "los-angeles",
                                                "doordash driver pay per hour los angeles 2026",
                                                "average doordash pay in los angeles after expenses",
                                                "Los Angeles needs a zone-based explanation because mileage and parking can erase a strong gross order market."),
                                buildDoorDashHourlyPaySnapshot(
                                                "chicago",
                                                "doordash driver hourly pay chicago 2026",
                                                "how much do doordash drivers make in chicago after expenses",
                                                "Chicago is useful for comparing dense restaurant demand against parking tickets, winter slowdown, and active-time friction."));
        }

        private CityEarningsSnapshot buildDoorDashHourlyPaySnapshot(
                        String citySlug,
                        String primaryQuery,
                        String secondaryQuery,
                        String acquisitionNote) {
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException(
                                                "City not found"));
                CityScenario scenario = generateScenarioByWorkLevel(city, "doordash", WorkLevel.SIDE_HUSTLE);
                return new CityEarningsSnapshot(city, scenario, primaryQuery, secondaryQuery, acquisitionNote);
        }

        private String buildUberHourlyReportJsonLd(
                        List<CityEarningsSnapshot> snapshots,
                        String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "City Earnings Reports",
                                                AppConstants.BASE_URL + "/salary/directory"),
                                buildBreadcrumbItem(3, "Uber Driver Hourly Earnings 2026", canonicalUrl)));

                List<Map<String, Object>> itemListElements = new ArrayList<>();
                for (int i = 0; i < snapshots.size(); i++) {
                        CityEarningsSnapshot snapshot = snapshots.get(i);
                        Map<String, Object> listItem = new LinkedHashMap<>();
                        listItem.put("@type", "ListItem");
                        listItem.put("position", i + 1);
                        listItem.put("name", String.format(
                                        "Uber driver hourly earnings in %s, %s",
                                        snapshot.city().getCityName(),
                                        snapshot.city().getState()));
                        listItem.put("url", String.format(
                                        "%s/salary/uber/%s",
                                        AppConstants.BASE_URL,
                                        snapshot.city().getSlug()));
                        itemListElements.add(listItem);
                }

                Map<String, Object> itemList = new LinkedHashMap<>();
                itemList.put("@type", "ItemList");
                itemList.put("name", "Uber driver hourly earnings city snapshots");
                itemList.put("itemListElement", itemListElements);

                Map<String, Object> article = new LinkedHashMap<>();
                article.put("@type", "Article");
                article.put("headline", "Uber Driver Hourly Earnings 2026: City Net Pay Report");
                article.put("url", canonicalUrl);
                article.put("dateModified", AppConstants.SITEMAP_LASTMOD_DATE);
                article.put("description",
                                "City-level Uber driver hourly earnings estimates after mileage and self-employment tax assumptions.");
                article.put("isAccessibleForFree", true);

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, article, itemList));
                return toJsonLd(graph);
        }

        private String buildDoorDashHourlyReportJsonLd(
                        List<CityEarningsSnapshot> snapshots,
                        String canonicalUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "City Earnings Reports",
                                                AppConstants.BASE_URL + "/salary/directory"),
                                buildBreadcrumbItem(3, "DoorDash Driver Hourly Pay 2026", canonicalUrl)));

                List<Map<String, Object>> itemListElements = new ArrayList<>();
                for (int i = 0; i < snapshots.size(); i++) {
                        CityEarningsSnapshot snapshot = snapshots.get(i);
                        Map<String, Object> listItem = new LinkedHashMap<>();
                        listItem.put("@type", "ListItem");
                        listItem.put("position", i + 1);
                        listItem.put("name", String.format(
                                        "DoorDash driver hourly pay in %s, %s",
                                        snapshot.city().getCityName(),
                                        snapshot.city().getState()));
                        listItem.put("url", String.format(
                                        "%s/salary/doordash/%s",
                                        AppConstants.BASE_URL,
                                        snapshot.city().getSlug()));
                        itemListElements.add(listItem);
                }

                Map<String, Object> itemList = new LinkedHashMap<>();
                itemList.put("@type", "ItemList");
                itemList.put("name", "DoorDash driver hourly pay city snapshots");
                itemList.put("itemListElement", itemListElements);

                Map<String, Object> article = new LinkedHashMap<>();
                article.put("@type", "Article");
                article.put("headline", "DoorDash Driver Hourly Pay 2026: City Earnings Report");
                article.put("url", canonicalUrl);
                article.put("dateModified", AppConstants.SITEMAP_LASTMOD_DATE);
                article.put("description",
                                "City-level DoorDash driver hourly pay estimates after mileage and self-employment tax assumptions.");
                article.put("isAccessibleForFree", true);

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, article, itemList));
                return toJsonLd(graph);
        }

        private String formatTopCityList(List<CityRankingDto> rankedCities, int limit) {
                List<String> cityNames = rankedCities.stream()
                                .limit(limit)
                                .map(dto -> dto.city().getCityName())
                                .collect(Collectors.toList());

                if (cityNames.isEmpty()) {
                        return "";
                }
                if (cityNames.size() == 1) {
                        return cityNames.get(0);
                }
                if (cityNames.size() == 2) {
                        return cityNames.get(0) + " and " + cityNames.get(1);
                }
                return String.join(", ", cityNames.subList(0, cityNames.size() - 1))
                                + ", and "
                                + cityNames.get(cityNames.size() - 1);
        }

        private String buildCalculatorUrl(String app, CityScenario scenario, CityData city) {
                return String.format(java.util.Locale.US,
                                "/%s?gross=%d&miles=%d&hours=%d&gasPrice=%.2f",
                                app,
                                scenario.getGrossWeekly(),
                                scenario.getMiles(),
                                scenario.getHours(),
                                city.getGasPrice());
        }

        private String buildTaxEstimatorUrl(String app, CityScenario scenario) {
                return String.format(java.util.Locale.US,
                                "/taxes/quarterly-estimator?app=%s&gross=%d",
                                app,
                                scenario.getGrossWeekly());
        }

        private String buildCityCalculatorJsonLd(
                        String appName,
                        CityData city,
                        CityScenario scenario,
                        String canonicalUrl,
                        String calculatorUrl) {
                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", List.of(
                                buildBreadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                                buildBreadcrumbItem(2, "City Earnings Reports",
                                                AppConstants.BASE_URL + "/salary/directory"),
                                buildBreadcrumbItem(3,
                                                String.format("%s %s Driver Earnings", city.getCityName(), appName),
                                                canonicalUrl)));

                Map<String, Object> offer = new LinkedHashMap<>();
                offer.put("@type", "Offer");
                offer.put("price", "0");
                offer.put("priceCurrency", "USD");

                Map<String, Object> webApplication = new LinkedHashMap<>();
                webApplication.put("@type", "WebApplication");
                webApplication.put("name", String.format("%s %s, %s Driver Earnings Estimate",
                                appName,
                                city.getCityName(),
                                city.getState()));
                webApplication.put("applicationCategory", "FinanceApplication");
                webApplication.put("operatingSystem", "Web");
                webApplication.put("isAccessibleForFree", true);
                webApplication.put("url", canonicalUrl);
                webApplication.put("sameAs", AppConstants.BASE_URL + calculatorUrl);
                webApplication.put("description", String.format(
                                "Estimated %s driver earnings for %s, %s with a baseline of $%.2f per hour net after mileage and self-employment tax plus a prefilled calculator link.",
                                appName,
                                city.getCityName(),
                                city.getState(),
                                scenario.getNetHourly()));
                webApplication.put("featureList", List.of(
                                "Net hourly pay estimate",
                                "Weekly gross pay baseline",
                                "Mileage cost assumption",
                                "Self-employment tax estimate",
                                "Prefilled app calculator link"));
                webApplication.put("offers", offer);

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, webApplication));
                return toJsonLd(graph);
        }

        private String buildWorkLevelJsonLd(
                        CityData city,
                        String appName,
                        WorkLevel workLevel,
                        CityScenario scenario,
                        String otherAppName,
                        String parentPageUrl,
                        String canonicalUrl,
                        String calculatorUrl) {
                List<Map<String, Object>> breadcrumbItems = new ArrayList<>();
                Map<String, Object> crumb1 = new LinkedHashMap<>();
                crumb1.put("@type", "ListItem");
                crumb1.put("position", 1);
                crumb1.put("name", "City Earnings Reports");
                crumb1.put("item", AppConstants.BASE_URL + "/salary/directory");
                breadcrumbItems.add(crumb1);

                Map<String, Object> crumb2 = new LinkedHashMap<>();
                crumb2.put("@type", "ListItem");
                crumb2.put("position", 2);
                crumb2.put("name", String.format("%s %s Driver Earnings", city.getCityName(), appName));
                crumb2.put("item", AppConstants.BASE_URL + parentPageUrl);
                breadcrumbItems.add(crumb2);

                Map<String, Object> crumb3 = new LinkedHashMap<>();
                crumb3.put("@type", "ListItem");
                crumb3.put("position", 3);
                crumb3.put("name", workLevel.getDisplayName());
                breadcrumbItems.add(crumb3);

                Map<String, Object> breadcrumb = new LinkedHashMap<>();
                breadcrumb.put("@type", "BreadcrumbList");
                breadcrumb.put("itemListElement", breadcrumbItems);

                Map<String, Object> offer = new LinkedHashMap<>();
                offer.put("@type", "Offer");
                offer.put("price", "0");
                offer.put("priceCurrency", "USD");

                Map<String, Object> webApplication = new LinkedHashMap<>();
                webApplication.put("@type", "WebApplication");
                webApplication.put("name", String.format("%s %s %s Earnings Estimate",
                                appName,
                                city.getCityName(),
                                workLevel.getDisplayName()));
                webApplication.put("applicationCategory", "FinanceApplication");
                webApplication.put("operatingSystem", "Web");
                webApplication.put("isAccessibleForFree", true);
                webApplication.put("url", canonicalUrl);
                webApplication.put("sameAs", AppConstants.BASE_URL + calculatorUrl);
                webApplication.put("description", String.format(
                                "Estimated %s earnings for %s %s driving with a $%.2f per hour net baseline and a prefilled calculator link.",
                                appName,
                                workLevel.getDisplayName().toLowerCase(java.util.Locale.US),
                                city.getCityName(),
                                scenario.getNetHourly()));
                webApplication.put("featureList", List.of(
                                "Work-level net pay estimate",
                                "Weekly gross pay baseline",
                                "Mileage and tax assumptions",
                                "Prefilled app calculator link"));
                webApplication.put("offers", offer);

                List<Map<String, Object>> mainEntity = new ArrayList<>();
                Map<String, Object> q1 = new LinkedHashMap<>();
                q1.put("@type", "Question");
                q1.put("name",
                                String.format("How much does a %s %s driver make in %s?",
                                                workLevel.getDisplayName(),
                                                appName,
                                                city.getCityName()));
                Map<String, Object> q1Answer = new LinkedHashMap<>();
                q1Answer.put("@type", "Answer");
                q1Answer.put("text",
                                String.format(
                                                "A %s %s driver in %s working %d hours per week can expect to take home approximately $%.2f per week after deducting estimated mileage and self-employment taxes.",
                                                workLevel.getDisplayName(),
                                                appName,
                                                city.getCityName(),
                                                workLevel.getHoursPerWeek(),
                                                scenario.getNetProfit()));
                q1.put("acceptedAnswer", q1Answer);
                mainEntity.add(q1);

                Map<String, Object> q2 = new LinkedHashMap<>();
                q2.put("@type", "Question");
                q2.put("name",
                                String.format("Is %s better than %s for %s drivers in %s?",
                                                appName,
                                                otherAppName,
                                                workLevel.getDisplayName(),
                                                city.getCityName()));
                Map<String, Object> q2Answer = new LinkedHashMap<>();
                q2Answer.put("@type", "Answer");
                q2Answer.put("text",
                                String.format(
                                                "Local factors like %s demand and $%.2f per gallon gas prices affect profitability. Compare this page with %s data to see which app fits your market timing.",
                                                city.getMarketTier(),
                                                city.getGasPrice(),
                                                otherAppName));
                q2.put("acceptedAnswer", q2Answer);
                mainEntity.add(q2);

                Map<String, Object> faqPage = new LinkedHashMap<>();
                faqPage.put("@type", "FAQPage");
                faqPage.put("mainEntity", mainEntity);

                Map<String, Object> graph = new LinkedHashMap<>();
                graph.put("@context", "https://schema.org");
                graph.put("@graph", List.of(breadcrumb, webApplication, faqPage));
                return toJsonLd(graph);
        }

        private Map<String, Object> buildFaqQuestion(String question, String answerText) {
                Map<String, Object> answer = new LinkedHashMap<>();
                answer.put("@type", "Answer");
                answer.put("text", answerText);

                Map<String, Object> questionMap = new LinkedHashMap<>();
                questionMap.put("@type", "Question");
                questionMap.put("name", question);
                questionMap.put("acceptedAnswer", answer);
                return questionMap;
        }

        private Map<String, Object> buildBreadcrumbItem(int position, String name, String itemUrl) {
                Map<String, Object> breadcrumbItem = new LinkedHashMap<>();
                breadcrumbItem.put("@type", "ListItem");
                breadcrumbItem.put("position", position);
                breadcrumbItem.put("name", name);
                breadcrumbItem.put("item", itemUrl);
                return breadcrumbItem;
        }

        private String toJsonLd(Object value) {
                try {
                        return OBJECT_MAPPER.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                        throw new IllegalStateException("Failed to serialize JSON-LD payload", e);
                }
        }

        private String editorialReviewLabel(String contentType) {
                if (contentType == null || contentType.isBlank()) {
                        return "Editorial review status unavailable";
                }

                String normalized = contentType.trim().toLowerCase(java.util.Locale.US);
                return switch (normalized) {
                        case "user_submitted" -> "Editorial review pending (user-submitted source)";
                        case "verified_interview" -> "Editorially reviewed (verified interview source)";
                        case "editorial_composite" -> "Editorially reviewed (composite source synthesis)";
                        default -> "Editorial review status: " + normalized;
                };
        }

        private List<CityScenario> generateScenarios(CityData city, String app) {
                List<CityScenario> scenarios = new ArrayList<>();
                CityData.MarketTier tier = city.getMarketTier();

                // Scenario 1: Part-time (10 hrs/week)
                scenarios.add(calculateScenario("Part-time (10 hrs/wk)", tier.getPartTimeGross(),
                                100, 10, city, app));

                // Scenario 2: Side-Hustle (25 hrs/week)
                scenarios.add(calculateScenario("Side-Hustle (25 hrs/wk)", tier.getSideHustleGross(),
                                250, 25, city, app));

                // Scenario 3: Full-time (40 hrs/week)
                scenarios.add(calculateScenario("Full-time (40 hrs/wk)", tier.getFullTimeGross(),
                                400, 40, city, app));

                return scenarios;
        }

        private CityScenario generateScenarioByWorkLevel(CityData city, String app, WorkLevel workLevel) {
                CityData.MarketTier tier = city.getMarketTier();

                return switch (workLevel) {
                        case PART_TIME -> calculateScenario(
                                        workLevel.getDisplayName() + " (" + workLevel.getHoursPerWeek() + " hrs/wk)",
                                        tier.getPartTimeGross(), workLevel.getMilesPerWeek(),
                                        workLevel.getHoursPerWeek(),
                                        city, app);
                        case SIDE_HUSTLE -> calculateScenario(
                                        workLevel.getDisplayName() + " (" + workLevel.getHoursPerWeek() + " hrs/wk)",
                                        tier.getSideHustleGross(), workLevel.getMilesPerWeek(),
                                        workLevel.getHoursPerWeek(),
                                        city, app);
                        case FULL_TIME -> calculateScenario(
                                        workLevel.getDisplayName() + " (" + workLevel.getHoursPerWeek() + " hrs/wk)",
                                        tier.getFullTimeGross(), workLevel.getMilesPerWeek(),
                                        workLevel.getHoursPerWeek(),
                                        city, app);
                };
        }

        private CityScenario calculateScenario(String name, int baseGross, int baseMiles, int baseHours, CityData city,
                        String app) {
                // City-specific factor adjustments
                double wageProxy = calculateWageProxy(city);
                double appMultiplier = app.equals("uber") ? 1.0 : 0.95; // Small variance for app

                // Adjust gross using local economy proxy
                double grossAdjusted = baseGross * wageProxy * appMultiplier;

                // Traffic Factor: < 1.0 means congested in CityData.
                // Ergo, dividing by trafficFactor INCREASES hours (e.g. 10 / 0.65 = 15.3 hrs).
                double adjustedHours = baseHours / city.getTrafficFactor();

                // Keep miles slightly increased in congested areas due to detours, but mostly
                // stable
                double milesAdjusted = baseMiles * (1.0 + (1.0 - city.getTrafficFactor()) * 0.3);

                // Mode A (IRS proxy) - standard deduction reflects gas, maintenance, and
                // depreciation
                double mileageDeduction = milesAdjusted * AppConstants.IRS_MILEAGE_RATE;
                double taxableProfit = grossAdjusted - mileageDeduction;

                // Taxes cannot be negative
                double taxes = Math.max(0, taxableProfit * AppConstants.SELF_EMPLOYMENT_TAX_RATE);

                // Final net profit (Gross minus IRS Proxy minus Taxes)
                double netProfit = grossAdjusted - mileageDeduction - taxes;
                double netHourly = adjustedHours > 0 ? netProfit / adjustedHours : 0;

                return new CityScenario(name, (int) grossAdjusted, (int) milesAdjusted, (int) adjustedHours, netProfit,
                                netHourly);
        }

        /**
         * Keep city-level spread without letting high-wage markets explode the model.
         * sqrt() preserves differentiation better than hard clamping.
         */
        private double calculateWageProxy(CityData city) {
                double rawWageProxy = city.getMinWage() / 7.25;
                double dampenedProxy = Math.sqrt(rawWageProxy);
                return Math.max(0.85, Math.min(1.85, dampenedProxy));
        }
}
