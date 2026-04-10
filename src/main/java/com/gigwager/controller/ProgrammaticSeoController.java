package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;
import com.gigwager.model.SeoMeta;
import com.gigwager.model.WorkLevel;
import com.gigwager.model.CityLocalData;
import com.gigwager.model.content.CityRichContent;
import com.gigwager.model.content.WorkLevelRichContent;
import com.gigwager.util.AppConstants;
import com.gigwager.service.CityRichContentRepository;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.HtmlSanitizerService;
import com.gigwager.service.PageIndexPolicyService;
import com.gigwager.dto.CityRankingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

        public ProgrammaticSeoController(DataLayerService dataLayerService,
                        PageIndexPolicyService pageIndexPolicyService,
                        CityRichContentRepository cityRichContentRepository,
                        HtmlSanitizerService htmlSanitizerService) {
                this.dataLayerService = dataLayerService;
                this.pageIndexPolicyService = pageIndexPolicyService;
                this.cityRichContentRepository = cityRichContentRepository;
                this.htmlSanitizerService = htmlSanitizerService;
        }

        @GetMapping("/salary/{app}")
        public String appHubPage(@PathVariable("app") String app, Model model) {
                if (!app.equals("uber") && !app.equals("doordash")) {
                        throw new com.gigwager.exception.ResourceNotFoundException("App not found");
                }
                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                List<CityRankingDto> topCities = Arrays.stream(CityData.values())
                                .filter(pageIndexPolicyService::isCityReportIndexable)
                                .map(city -> {
                                        CityScenario scenario = calculateScenario("Side-Hustle",
                                                        city.getMarketTier().getSideHustleGross(), 250, 25, city, app);
                                        return new CityRankingDto(city, scenario.getNetHourly(), "Side-Hustle");
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
                                .filter(pageIndexPolicyService::isCityReportIndexable)
                                .count();

                // Dynamic Date
                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                CityRankingDto comparisonCity = topCities.stream()
                                .filter(dto -> dataLayerService.hasRichLocalData(dto.city().getSlug()))
                                .findFirst()
                                .orElse(null);

                String coveragePath = app.equals("uber") ? "/uber/where-you-can-drive" : "/doordash/where-you-can-dash";

                String title = String.format("%s Earnings by City After Expenses: Is It Worth It?", appName);
                String description = String.format(
                                "Is %s worth it in your market? Start with %d city reports, compare city winners, run the calculator, and use the coverage guide before assuming a market is active. Updated %s.",
                                appName,
                                indexedCityCount,
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
                                .filter(pageIndexPolicyService::isCityReportIndexable)
                                .map(city -> {
                                        CityScenario scenario = calculateScenario("Side-Hustle",
                                                        city.getMarketTier().getSideHustleGross(), 250, 25, city, app);
                                        return new CityRankingDto(city, scenario.getNetHourly(), "Side-Hustle");
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

                String title = String.format("Highest-Paying Cities for %s Drivers in %d | After-Expenses Ranking",
                                appName,
                                currentYear);
                String description = String.format(
                                "See which %s cities rank highest in %d based on estimated take-home pay after mileage and self-employment tax. %s currently leads at about $%.2f/hr net. View the top 10 and open your city report.",
                                appName,
                                currentYear,
                                topRankedCity.city().getCityName(),
                                topRankedCity.netHourly());
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

                // PR8-3: Only generate for cities with rich local data AND passing policy
                if (!dataLayerService.hasRichLocalData(citySlug)
                                || !pageIndexPolicyService.isCityReportIndexable(city)) {
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
                        title = String.format("Uber vs DoorDash in %s: Which Pays More?",
                                        city.getCityName());
                        description = String.format(
                                        "Which app is worth it in %s? Side-hustle estimates put Uber and DoorDash near $%.2f/hr net after mileage and tax assumptions. Updated %s.",
                                        city.getCityName(),
                                        winningNetHourly,
                                        monthYear);
                } else {
                        title = String.format("Uber vs DoorDash in %s: Which Pays More?",
                                        city.getCityName());
                        description = String.format(
                                        "Which app is worth it in %s? Current side-hustle estimates put %s at $%.2f/hr net, about $%.2f/hr ahead of %s after mileage and tax assumptions. Updated %s.",
                                        city.getCityName(),
                                        winningAppName,
                                        winningNetHourly,
                                        netHourlyGap,
                                        losingAppName,
                                        monthYear);
                }
                String canonicalUrl = String.format("%s/compare/%s/uber-vs-doordash", AppConstants.BASE_URL, citySlug);

                model.addAttribute("city", city);
                model.addAttribute("uberScenario", uberScenario);
                model.addAttribute("doordashScenario", doordashScenario);
                model.addAttribute("winningAppName", winningAppName);
                model.addAttribute("losingAppName", losingAppName);
                model.addAttribute("winningNetHourly", winningNetHourly);
                model.addAttribute("netHourlyGap", netHourlyGap);
                model.addAttribute("nearlyTied", nearlyTied);
                model.addAttribute("lastUpdated", monthYear);
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

                String title = String.format("Average %s Driver Earnings in %s (%d): $%.2f/hr After Expenses",
                                appName, city.getCityName(), now.getYear(), featuredScenario.getNetHourly());
                String description = String.format(
                                "Average %s driver earnings in %s for %d run about $%.2f/hr net in our side-hustle model after mileage and self-employment tax. See part-time, side-hustle, and full-time scenarios. Updated %s.",
                                appName, city.getCityName(), now.getYear(), featuredScenario.getNetHourly(), monthYear);
                String heroTitlePrimary = String.format("Average %s Driver Earnings in %s", appName,
                                city.getCityName());
                String heroTitleSecondary = String.format("%d After-Expenses Estimate", now.getYear());
                String heroTitleTertiary = "Part-Time, Side-Hustle, and Full-Time Views";
                String heroSummary = String.format(
                                "Average %s driver earnings in %s currently run about $%.2f/hr net in our side-hustle model after mileage, self-employment tax, and local gas costs. Baseline scenario: $%d/week gross, %d mi, %d hrs. Local gas: $%.2f/gal.",
                                appName,
                                city.getCityName(),
                                featuredScenario.getNetHourly(),
                                featuredScenario.getGrossWeekly(),
                                featuredScenario.getMiles(),
                                featuredScenario.getHours(),
                                city.getGasPrice());

                String canonicalUrl = String.format("%s/salary/%s/%s", AppConstants.BASE_URL, app, citySlug);
                String appHubCanonicalUrl = String.format("%s/salary/%s", AppConstants.BASE_URL, app);

                // Cross-App Silo: Generate link to the other app
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";
                String otherAppUrl = String.format("/salary/%s/%s", otherApp, citySlug);

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
                model.addAttribute("methodologyUrl", "/methodology");
                model.addAttribute("calculatorUrl", buildCalculatorUrl(app, featuredScenario, city));
                model.addAttribute("taxEstimatorUrl", buildTaxEstimatorUrl(app, featuredScenario));
                model.addAttribute("bestCitiesUrl", String.format("/best-cities/%s", app));
                model.addAttribute("compareUrl",
                                dataLayerService.hasRichLocalData(citySlug)
                                                ? String.format("/compare/%s/uber-vs-doordash", citySlug)
                                                : "");
                model.addAttribute("safeMarketDescription", htmlSanitizerService.sanitize(city.getMarketDescription()));
                model.addAttribute("cityFaqJsonLd", buildCityFaqJsonLd(appName, city, featuredScenario));

                boolean cityIndexable = pageIndexPolicyService.isCityReportIndexable(city)
                                && featuredScenario.getNetHourly() >= 6.0
                                && featuredScenario.getNetHourly() <= 45.0;
                if (!cityIndexable) {
                        model.addAttribute("noIndex", true);
                        canonicalUrl = appHubCanonicalUrl;
                }

                // Pass raw description to template if needed, or rely on SeoMeta
                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                AppConstants.BASE_URL + "/og-image.jpg"));

                // Internal Linking Silo: 3 random cities with same MarketTier
                List<CityData> similarCities = Arrays.stream(CityData.values())
                                .filter(c -> c.getMarketTier() == city.getMarketTier()) // Same Economy Tier
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

                // Resolve work level from slug
                WorkLevel workLevel;
                try {
                        workLevel = WorkLevel.fromSlug(workLevelSlug);
                } catch (IllegalArgumentException e) {
                        throw new com.gigwager.exception.ResourceNotFoundException("Work level not found");
                }

                // Generate scenario for this specific work level
                CityScenario scenario = generateScenarioByWorkLevel(city, app, workLevel);

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                // Build unique SEO meta
                String appName = app.equals("uber") ? "Uber" : "DoorDash";
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";

                String title = String.format("%s %s in %s: $%.2f/hr After Expenses",
                                appName, workLevel.getDisplayName(), city.getCityName(),
                                scenario.getNetHourly());
                String description = String.format(
                                "%s %s in %s runs about $%.2f/hr net after mileage and SE tax. Includes local strategy, calculator links, and %s comparison.",
                                appName, workLevel.getDisplayName(), city.getCityName(),
                                scenario.getNetHourly(), otherAppName);

                String canonicalUrl = String.format("%s/salary/%s/%s/%s", AppConstants.BASE_URL, app, citySlug,
                                workLevelSlug);

                // Cross-App Silo
                String otherAppUrl = String.format("/salary/%s/%s/%s", otherApp, citySlug, workLevelSlug);

                // Parent page (main city report) for breadcrumb
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

                boolean workLevelIndexable = pageIndexPolicyService.isWorkLevelReportIndexable(city, workLevel)
                                && scenario.getNetHourly() >= 6.0
                                && scenario.getNetHourly() <= 45.0;
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
                                parentPageUrl));

                // Internal Linking Silo: 3 random cities with same MarketTier
                List<CityData> similarCities = Arrays.stream(CityData.values())
                                .filter(c -> c.getMarketTier() == city.getMarketTier()) // Same Economy Tier
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

        private String chooseNonBlank(String primary, String fallback) {
                if (primary != null && !primary.isBlank()) {
                        return primary;
                }
                return fallback;
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
                itemList.put("name", String.format("Highest-paying cities for %s drivers in %d", appName,
                                java.time.LocalDate.now().getYear()));
                itemList.put("description",
                                String.format(
                                                "Rankings of US cities based on estimated %s take-home pay after mileage and self-employment tax assumptions, with city report links for each market.",
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
                                "In GigVerdict's current after-expenses ranking, %s leads at about $%.2f per hour net after mileage and self-employment tax assumptions.",
                                topCity.city().getCityName(),
                                topCity.netHourly());
                String q2 = String.format("Is this a coverage list or an earnings ranking for %s?", appName);
                String a2 = app.equals("uber")
                                ? "This page is an earnings ranking. Use the Uber coverage guide and Uber's official city directory when your question is whether a market is active."
                                : "This page is an earnings ranking. Use the DoorDash availability guide and DoorDash's Dasher signup flow when your question is whether you can dash in a market.";
                String q3 = String.format("How many %s city reports are ranked here?", appName);
                String a3 = String.format(
                                "GigVerdict currently ranks %d U.S. city reports for %s and links each market to a deeper city earnings page.",
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
                breadcrumbItems.add(buildBreadcrumbItem(2, "Salary Directory", AppConstants.BASE_URL + "/salary/directory"));
                breadcrumbItems.add(buildBreadcrumbItem(3,
                                String.format("%s Earnings by City", appName),
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
                itemList.put("name", String.format("%s earnings by city reports", appName));
                itemList.put("itemListOrder", "https://schema.org/ItemListOrderDescending");
                itemList.put("numberOfItems", itemListElements.size());
                itemList.put("itemListElement", itemListElements);

                CityRankingDto topCity = topCities.get(0);
                String q1 = String.format("How do %s earnings by city look in 2026?", appName);
                String a1 = String.format(
                                "GigVerdict currently tracks %d %s city reports. In the current side-hustle ranking, %s leads at about $%.2f per hour net after mileage and self-employment tax assumptions. Open any city report to compare part-time, side-hustle, and full-time scenarios.",
                                indexedCityCount,
                                appName,
                                topCity.city().getCityName(),
                                topCity.netHourly());

                String q2 = String.format("Is this an official %s coverage list?", appName);
                String a2 = app.equals("uber")
                                ? "No. This hub compares estimated pay by city. Use the separate Uber coverage guide and Uber's official city directory to confirm that a market is active before assuming coverage."
                                : "No. This hub compares estimated pay by city and is not an official coverage directory. Check DoorDash's own onboarding flow or local app availability to confirm that a market is active.";

                String q3 = String.format("What does each %s city report include?", appName);
                String a3 = "Each city report includes part-time, side-hustle, and full-time earnings scenarios, mileage-based cost assumptions, quarterly tax context, and links to the calculator so you can adjust the numbers for your own routine.";

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
                String q1 = String.format("How much does a %s driver make in %s after expenses in 2026?",
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

                String q3 = String.format("How are these %s earnings calculated?", appName);
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

                String q6 = String.format("Why do %s earnings in %s differ from other cities?",
                                appName,
                                city.getCityName());
                String trafficDescriptor = city.isHighTraffic()
                                ? "heavy traffic congestion that increases hours per delivery"
                                : "moderate traffic conditions";
                String a6 = String.format(
                                "Earnings vary due to local factors. %s has gas at $%.2f/gal, %s, and is classified as a %s market. High-cost cities can have higher gross pay and higher expenses, while lower-cost cities may provide better effective margins.",
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

                String q2 = String.format("How many %s city pay reports does GigVerdict cover right now?", appName);
                String a2 = String.format(
                                "GigVerdict currently links %d covered city pay reports for %s. Each report focuses on net hourly earnings after mileage, fuel, and self-employment tax assumptions.",
                                coveredCityCount,
                                appName);

                String q3 = String.format("What should I do after I confirm my %s city is active?", appName);
                String a3 = String.format(
                                "Open the matching GigVerdict city report to compare estimated take-home pay, then review the best-cities ranking if you are deciding between markets or planning a move for %s work.",
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
                                .filter(pageIndexPolicyService::isCityReportIndexable)
                                .sorted((left, right) -> left.getCityName().compareTo(right.getCityName()))
                                .collect(Collectors.toList());

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                String title = String.format("Where You Can %s for %s in the US (%s)", coverageVerb, appName, monthYear);
                String description = String.format(
                                "%s Compare take-home pay across %d GigVerdict city reports after you verify local availability. Updated %s.",
                                officialSourceSummary,
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

        private String buildWorkLevelJsonLd(
                        CityData city,
                        String appName,
                        WorkLevel workLevel,
                        CityScenario scenario,
                        String otherAppName,
                        String parentPageUrl) {
                List<Map<String, Object>> breadcrumbItems = new ArrayList<>();
                Map<String, Object> crumb1 = new LinkedHashMap<>();
                crumb1.put("@type", "ListItem");
                crumb1.put("position", 1);
                crumb1.put("name", "Salary Directory");
                crumb1.put("item", AppConstants.BASE_URL + "/salary/directory");
                breadcrumbItems.add(crumb1);

                Map<String, Object> crumb2 = new LinkedHashMap<>();
                crumb2.put("@type", "ListItem");
                crumb2.put("position", 2);
                crumb2.put("name", String.format("%s %s Earnings", city.getCityName(), appName));
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
                graph.put("@graph", List.of(breadcrumb, faqPage));
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
