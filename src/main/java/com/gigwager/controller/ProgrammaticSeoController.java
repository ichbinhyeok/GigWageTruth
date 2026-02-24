package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;
import com.gigwager.model.SeoMeta;
import com.gigwager.model.WorkLevel;
import com.gigwager.model.CityLocalData;
import com.gigwager.util.AppConstants;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.PageIndexPolicyService;
import com.gigwager.dto.CityRankingDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProgrammaticSeoController {

        private final DataLayerService dataLayerService;
        private final PageIndexPolicyService pageIndexPolicyService;

        public ProgrammaticSeoController(DataLayerService dataLayerService,
                        PageIndexPolicyService pageIndexPolicyService) {
                this.dataLayerService = dataLayerService;
                this.pageIndexPolicyService = pageIndexPolicyService;
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
                                        CityScenario scenario = calculateScenario("Full-time",
                                                        city.getMarketTier().getFullTimeGross(), 400, 40, city, app);
                                        return new CityRankingDto(city, scenario.getNetHourly(), "Full-time");
                                })
                                .filter(dto -> dto.netHourly() >= 6.0 && dto.netHourly() <= 45.0) // Sanity Gate
                                .sorted((c1, c2) -> Double.compare(c2.netHourly(), c1.netHourly()))
                                .limit(10)
                                .collect(Collectors.toList());

                // Dynamic Date
                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                String title = String.format("%s Pay Calculator & City Directory (%s)", appName, monthYear);
                String description = String.format(
                                "Learn how much %s drivers actually make after vehicle expenses and taxes. Find real net hourly wage estimates for top cities before you drive.",
                                appName);
                String canonicalUrl = String.format("%s/salary/%s", AppConstants.BASE_URL, app);

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("topCities", topCities);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));

                return "salary/app-hub";
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

                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                String title = String.format("Best Cities for %s Drivers (%s Rankings)", appName, monthYear);
                String description = String.format(
                                "We ranked the best cities to drive for %s based on real net hourly take-home pay after deducting local gas prices and taxes.",
                                appName);
                String canonicalUrl = String.format("%s/best-cities/%s", AppConstants.BASE_URL, app);

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("rankedCities", rankedCities);
                model.addAttribute("lastUpdated", monthYear);
                model.addAttribute("seoMeta",
                                new SeoMeta(title, description, canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));
                model.addAttribute("dataLayerService", dataLayerService);

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

                String title = String.format("Uber vs DoorDash in %s: Which Pays More? (%s)", city.getCityName(),
                                monthYear);
                String description = String.format(
                                "Comparing Uber and DoorDash pay in %s. Discover which gig app offers a higher net hourly wage after adjusting for %s gas prices.",
                                city.getCityName(), city.getCityName());
                String canonicalUrl = String.format("%s/compare/%s/uber-vs-doordash", AppConstants.BASE_URL, citySlug);

                model.addAttribute("city", city);
                model.addAttribute("uberScenario", uberScenario);
                model.addAttribute("doordashScenario", doordashScenario);
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

                // Dynamic Date (Freshness Signal) - Force US Locale
                // Dynamic Date (Freshness Signal) - Force US Locale
                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);
                String fullDate = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                // Build unique SEO meta
                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                // CTR Strategy: Utility Gap + Curiosity Gap (No specific numbers in Title)
                // "Uber in Austin: Net Pay Calculator (Jan 2026) - Is It Worth It?"
                String title = String.format("%s in %s: Net Pay Calculator (%s) - Is It Worth It?",
                                appName, city.getCityName(), monthYear);

                // Meta Description: Fear/Loss Marketing + Utility Focus
                String description;
                String gasPrice = String.format("$%.2f", city.getGasPrice());

                if (city.isHighTraffic()) {
                        description = String.format(
                                        "Don't drive blind in %s. Traffic congestion eats your profit. Estimate your net hourly wage using the IRS mileage rate ($0.725/mi) plus taxes. Local gas: %s/gal.",
                                        city.getCityName(), gasPrice);
                } else if (city.isCheapGas()) {
                        description = String.format(
                                        "Gas is cheap in %s (%s/gal), but are you actually profiting after all costs? Use our %s Net Pay Calculator (IRS mileage proxy + SE tax) to see your real take-home.",
                                        city.getCityName(), gasPrice, appName);
                } else if (city.isHighCost()) {
                        description = String.format(
                                        "Is %s worth it in %s's high-cost market? We estimate net profit using the IRS standard mileage rate and SE tax to show the real breakdown. See the numbers.",
                                        appName, city.getCityName());
                } else {
                        description = String.format(
                                        "Stop guessing. Estimate your net hourly wage as a %s driver in %s. We use the IRS mileage rate ($0.725/mi) and 15.3%% SE tax to show your REAL profit.",
                                        appName, city.getCityName());
                }

                String canonicalUrl = String.format("%s/salary/%s/%s", AppConstants.BASE_URL, app, citySlug);

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

                if (!pageIndexPolicyService.isCityReportIndexable(city) || featuredScenario.getNetHourly() < 6.0
                                || featuredScenario.getNetHourly() > 45.0) {
                        model.addAttribute("noIndex", true);
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

                // Dynamic Date (Freshness Signal) - Force US Locale
                // Dynamic Date (Freshness Signal) - Force US Locale
                java.time.LocalDate now = java.time.LocalDate.now();
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.US)
                                .format(now);

                // Build unique SEO meta
                String appName = app.equals("uber") ? "Uber" : "DoorDash";

                // CTR Strategy: Utility Gap + Curiosity Gap
                // "Uber Part-Time in Austin: Net Pay Calculator (Jan 2026) - Is It Worth It?"
                String title = String.format("%s %s in %s: Net Pay Calculator (%s) - Is It Worth It?",
                                appName, workLevel.getDisplayName(), city.getCityName(), monthYear);

                // Meta Description: Utility Focused
                String description = String.format(
                                "Using the %s strategy (%s hrs/wk) in %s? Use our calculator to see your real net profit after self-employment tax and gas. Don't rely on gross numbers.",
                                workLevel.getDisplayName(), workLevel.getHoursPerWeek(), city.getCityName());

                String canonicalUrl = String.format("%s/salary/%s/%s/%s", AppConstants.BASE_URL, app, citySlug,
                                workLevelSlug);

                // Cross-App Silo
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";
                String otherAppUrl = String.format("/salary/%s/%s/%s", otherApp, citySlug, workLevelSlug);

                // Parent page (main city report) for breadcrumb
                String parentPageUrl = String.format("/salary/%s/%s", app, citySlug);

                // Freshness signal
                String lastUpdated = monthYear;

                // Fetch CityLocalData to replace placeholder tokens
                CityLocalData localData = dataLayerService.getLocalData(city.getSlug());

                // Generate unique content sections
                String workLevelMeaning = workLevel.getWorkLevelMeaning(appName, city.getCityName(), localData);
                String taxStrategy = workLevel.getTaxStrategy(appName, city.getCityName(), localData);
                String dayInTheLife = workLevel.getDayInTheLife(appName, city.getCityName(), city, localData);
                String bestPractices = workLevel.getBestPractices(appName, city.getCityName(), city, localData);

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

                if (!pageIndexPolicyService.isWorkLevelReportIndexable(city, workLevel) || scenario.getNetHourly() < 6.0
                                || scenario.getNetHourly() > 45.0) {
                        model.addAttribute("noIndex", true);
                }

                // Unique content sections
                model.addAttribute("workLevelMeaning", workLevelMeaning);
                model.addAttribute("taxStrategy", taxStrategy);
                model.addAttribute("dayInTheLife", dayInTheLife);
                model.addAttribute("bestPractices", bestPractices);

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

                return "salary/city-work-level";
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
                double rawWageProxy = city.getMinWage() / 7.25;
                double wageProxy = Math.max(0.9, Math.min(1.2, rawWageProxy)); // Realism Clamp: 0.9 to 1.2
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
}
