package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityIntentEvidence;
import com.gigwager.model.CityIntentMetric;
import com.gigwager.model.CityIntentPage;
import com.gigwager.model.CityScenario;
import com.gigwager.model.DriverFieldNote;
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

                String title = String.format("%s Driver Earnings by City After Expenses", appName);
                String description = String.format(
                                "Compare estimated %s driver earnings across %d cities after mileage and self-employment tax. %s currently leads at about $%.2f/hr net. Updated %s.",
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

                String title = String.format("Highest-Paying Cities for %s Drivers %d",
                                appName,
                                currentYear);
                String description = String.format(
                                "Ranked by estimated %s driver earnings after mileage and self-employment tax. %s leads at about $%.2f/hr net; each city page includes an adjustable calculator.",
                                appName,
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

                String title = String.format("%s Driver Earnings in %s %d: $%.2f/hr Net",
                                appName, city.getCityName(), now.getYear(),
                                featuredScenario.getNetHourly());
                String description = String.format(
                                "Estimated %s driver earnings in %s: $%.2f/hr net after mileage and SE tax on a 25-hour baseline. Adjust gross, miles, hours, and gas. Updated %s.",
                                appName, city.getCityName(), featuredScenario.getNetHourly(), monthYear);
                String heroTitlePrimary = String.format("%s Driver Earnings in %s", appName,
                                city.getCityName());
                String heroTitleSecondary = String.format("$%.2f/hr Net After Expenses",
                                featuredScenario.getNetHourly());
                String heroTitleTertiary = "Average Pay After Mileage and Tax";
                String heroSummary = String.format(
                                "Estimated %s driver earnings in %s start at $%.2f/hr net in our side-hustle model. The baseline uses $%d/week gross, %d mi, %d hrs, and $%.2f/gal gas; use the calculator below to adjust your own numbers.",
                                appName,
                                city.getCityName(),
                                featuredScenario.getNetHourly(),
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
                model.addAttribute("methodologyUrl", "/methodology");
                model.addAttribute("calculatorUrl", calculatorUrl);
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
                                parentPageUrl,
                                canonicalUrl,
                                calculatorUrl));

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

                boolean indexable = pageIndexPolicyService.isCityReportIndexable(city)
                                && scenario.getNetHourly() >= 6.0
                                && scenario.getNetHourly() <= 45.0;
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
                model.addAttribute("intentMetrics", buildCityIntentMetrics(city, intentPage, scenario));
                model.addAttribute("intentEvidencePatterns",
                                buildCityIntentEvidencePatterns(app, appName, city, intentPage, scenario));
                model.addAttribute("answerHtml", answerHtml);
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
                };
        }

        private String buildCityIntentAnswerHtml(
                        String appName,
                        CityData city,
                        CityIntentPage intentPage,
                        CityScenario scenario) {
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
                };
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
                                case "nashville" -> notes.add(new DriverFieldNote(
                                                "Nashville pattern",
                                                "Supply and event timing matter more than the city average",
                                                "Recent Nashville driver discussion frames the market as sensitive to out-of-state driver supply, weak regulation, and app transparency differences. For this page, the city average should be treated as a starting point, not a promise for random weekday hours.",
                                                "Nashville Uber/Lyft market discussion",
                                                "https://www.reddit.com/r/uberdrivers/comments/1qwnmas/hows_the_nashville_uberlyft_market_right_now/"));
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
                                case "denver" -> notes.add(new DriverFieldNote(
                                                "Denver pattern",
                                                "Centennial and suburb runs can pull the average down",
                                                "Denver-area dasher discussion specifically calls out a drop from stronger 2024 hourly results to roughly $12-$15/hr in Centennial-style suburban work. That is why the Denver estimate needs a mileage and zone check before a driver trusts it.",
                                                "Denver DoorDash driver discussion",
                                                "https://www.reddit.com/r/doordash_drivers/comments/1j4uofw/is_anybody_dashing_around_denver_area_if_yes_how/"));
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
                                .filter(pageIndexPolicyService::isCityReportIndexable)
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
