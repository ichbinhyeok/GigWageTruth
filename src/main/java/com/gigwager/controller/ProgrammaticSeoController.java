package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;
import com.gigwager.model.SeoMeta;
import com.gigwager.model.WorkLevel;
import com.gigwager.model.CityLocalData;
import com.gigwager.util.AppConstants;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.PageIndexPolicyService;
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
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("yyyy", java.util.Locale.US)
                                .format(now);
                String fullDate = java.time.format.DateTimeFormatter.ofPattern("yyyy", java.util.Locale.US)
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
                                        "Don't drive blind in %s. Traffic congestion thrives here. Estimate your TRUE liquid hourly wage after gas (%s/gal) and depreciation. See if you're actually making a profit.",
                                        city.getCityName(), gasPrice);
                } else if (city.isCheapGas()) {
                        description = String.format(
                                        "Gas is cheap in %s (%s/gal), but are you actually profiting? Don't be fooled by gross numbers. Use our %s Net Pay Calculator to estimate your real take-home pay.",
                                        city.getCityName(), gasPrice, appName);
                } else if (city.isHighCost()) {
                        description = String.format(
                                        "Is %s worth it in %s's high-cost market? Don't drive blind. We estimate the exact breakdown of Expenses vs. Profit for %s drivers. See the truth.",
                                        appName, city.getCityName(), appName);
                } else {
                        description = String.format(
                                        "Stop guessing. Estimate your true hourly wage as a %s driver in %s. We deduct estimated gas (%s/gal), taxes, and wear & tear to show your REAL profit.",
                                        appName, city.getCityName(), gasPrice);
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

                if (!pageIndexPolicyService.isCityReportIndexable(city)) {
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
                String monthYear = java.time.format.DateTimeFormatter.ofPattern("yyyy", java.util.Locale.US)
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

                if (!pageIndexPolicyService.isWorkLevelReportIndexable(city, workLevel)) {
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
                double wageProxy = Math.max(1.0, city.getMinWage() / 7.25); // Baseline federal min wage
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
                double mileageDeduction = milesAdjusted * AppConstants.IRS_MILEAGE_RATE_2024;
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
