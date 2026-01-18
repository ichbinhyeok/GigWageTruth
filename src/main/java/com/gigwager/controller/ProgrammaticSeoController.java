package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;
import com.gigwager.model.SeoMeta;
import com.gigwager.model.WorkLevel;
import com.gigwager.util.AppConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProgrammaticSeoController {

        @GetMapping("/salary/{app}/{citySlug}")
        public String citySalaryPage(@PathVariable("app") String app,
                        @PathVariable("citySlug") String citySlug,
                        Model model) {

                // Validate app
                if (!app.equals("uber") && !app.equals("doordash")) {
                        return "redirect:/";
                }

                // Resolve city from slug
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new IllegalArgumentException("City not found: " + citySlug));

                // Generate 3 scenarios based on MarketTier
                List<CityScenario> scenarios = generateScenarios(city, app);

                // Select "Featured" scenario (side-hustle level)
                CityScenario featuredScenario = scenarios.get(1);

                // Build unique SEO meta
                String appName = app.equals("uber") ? "Uber" : "DoorDash";
                // Question-based title for better CTR
                String title = String.format("How much do %s Drivers make in %s, %s? (2026 Real Numbers)",
                                appName, city.getCityName(), city.getState());

                // Branching Meta Description Logic (Anti-Pattern Detection)
                String description;
                double hourly = featuredScenario.getNetHourly();
                String gasPrice = String.format("$%.2f", city.getGasPrice());

                if (city.isHighTraffic()) {
                        description = String.format(
                                        "Stop traffic killing your hourly rate. See how %s drivers in %s navigate congestion to earn $%.2f/hr net after expenses.",
                                        appName, city.getCityName(), hourly);
                } else if (city.isCheapGas()) {
                        description = String.format(
                                        "Gas is cheap in %s (%s/gal), but are rates high enough? See the real take-home pay for %s drivers in 2026.",
                                        city.getCityName(), gasPrice, appName);
                } else if (city.isHighCost()) {
                        description = String.format(
                                        "Is %s worth it in %s given the high cost of living? We calculated the exact net hourly wage ($%.2f/hr) for local drivers.",
                                        appName, city.getCityName(), hourly);
                } else {
                        description = String.format(
                                        "Calculate your true hourly wage as a %s driver in %s after gas (%s/gal), vehicle depreciation, and taxes. Est: $%.2f/hr.",
                                        appName, city.getCityName(), gasPrice, hourly);
                }

                String canonicalUrl = String.format("%s/salary/%s/%s", AppConstants.BASE_URL, app, citySlug);

                // Cross-App Silo: Generate link to the other app
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";
                String otherAppUrl = String.format("/salary/%s/%s", otherApp, citySlug);

                // Freshness signal
                String lastUpdated = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")
                                .format(java.time.LocalDate.now());

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("city", city);
                model.addAttribute("scenarios", scenarios);
                model.addAttribute("featuredScenario", featuredScenario);
                model.addAttribute("lastUpdated", lastUpdated);
                model.addAttribute("otherApp", otherApp);
                model.addAttribute("otherAppName", otherAppName);
                model.addAttribute("otherAppUrl", otherAppUrl);
                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                AppConstants.BASE_URL + "/og-image.jpg"));

                // Internal Linking Silo: 3 random cities with same MarketTier
                List<CityData> similarCities = Arrays.stream(CityData.values())
                                .filter(c -> c.getMarketTier() == city.getMarketTier()) // Same Economy Tier
                                .filter(c -> !c.equals(city)) // Exclude current city
                                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                                        Collections.shuffle(collected); // Randomize for variety
                                        return collected.stream().limit(3).collect(Collectors.toList());
                                }));
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
                        return "redirect:/";
                }

                // Resolve city from slug
                CityData city = CityData.fromSlug(citySlug)
                                .orElseThrow(() -> new IllegalArgumentException("City not found: " + citySlug));

                // Resolve work level from slug
                WorkLevel workLevel;
                try {
                        workLevel = WorkLevel.fromSlug(workLevelSlug);
                } catch (IllegalArgumentException e) {
                        return "redirect:/salary/" + app + "/" + citySlug;
                }

                // Generate scenario for this specific work level
                CityScenario scenario = generateScenarioByWorkLevel(city, app, workLevel);

                // Build unique SEO meta
                String appName = app.equals("uber") ? "Uber" : "DoorDash";
                // Question-based title for better CTR
                String title = String.format("How much do %s Drivers make in %s? (%s Guide 2026)",
                                appName, city.getCityName(), workLevel.getDisplayName());

                String description = String.format(
                                "Deep dive into %s %s earnings in %s for %s drivers. Real take-home pay: $%.2f/hr. " +
                                                "Includes tax strategy, time management, and %s-specific tips for %s.",
                                appName, workLevel.getDisplayName().toLowerCase(), city.getCityName(),
                                workLevel.getDisplayName().toLowerCase(), scenario.getNetHourly(),
                                workLevel.getDisplayName().toLowerCase(), city.getCityName());

                String canonicalUrl = String.format("%s/salary/%s/%s/%s", AppConstants.BASE_URL, app, citySlug,
                                workLevelSlug);

                // Cross-App Silo
                String otherApp = app.equals("uber") ? "doordash" : "uber";
                String otherAppName = app.equals("uber") ? "DoorDash" : "Uber";
                String otherAppUrl = String.format("/salary/%s/%s/%s", otherApp, citySlug, workLevelSlug);

                // Parent page (main city report) for breadcrumb
                String parentPageUrl = String.format("/salary/%s/%s", app, citySlug);

                // Freshness signal
                String lastUpdated = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")
                                .format(java.time.LocalDate.now());

                // Generate unique content sections
                String workLevelMeaning = workLevel.getWorkLevelMeaning(appName, city.getCityName());
                String taxStrategy = workLevel.getTaxStrategy(appName, city.getCityName());
                String dayInTheLife = workLevel.getDayInTheLife(appName, city.getCityName(), city);
                String bestPractices = workLevel.getBestPractices(appName, city.getCityName(), city);

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

                // Unique content sections
                model.addAttribute("workLevelMeaning", workLevelMeaning);
                model.addAttribute("taxStrategy", taxStrategy);
                model.addAttribute("dayInTheLife", dayInTheLife);
                model.addAttribute("bestPractices", bestPractices);

                // JobPosting Schema Automation
                String validThrough = java.time.LocalDate.now().plusDays(180)
                                .format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
                String datePosted = java.time.LocalDate.now()
                                .format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);

                // Escape HTML for JSON-LD description
                String schemaDescription = workLevelMeaning.replace("\"", "\\\"").replace("\n", " ");

                model.addAttribute("validThrough", validThrough);
                model.addAttribute("datePosted", datePosted);
                model.addAttribute("schemaDescription", schemaDescription);

                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                AppConstants.BASE_URL + "/og-image.jpg"));

                // Internal Linking Silo: 3 random cities with same MarketTier
                List<CityData> similarCities = Arrays.stream(CityData.values())
                                .filter(c -> c.getMarketTier() == city.getMarketTier()) // Same Economy Tier
                                .filter(c -> !c.equals(city)) // Exclude current city
                                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                                        Collections.shuffle(collected); // Randomize for variety
                                        return collected.stream().limit(3).collect(Collectors.toList());
                                }));
                model.addAttribute("similarCities", similarCities);

                return "salary/city-work-level";
        }

        private List<CityScenario> generateScenarios(CityData city, String app) {
                List<CityScenario> scenarios = new ArrayList<>();
                CityData.MarketTier tier = city.getMarketTier();

                // Scenario 1: Part-time (10 hrs/week)
                scenarios.add(calculateScenario("Part-time (10 hrs/wk)", tier.getPartTimeGross(),
                                100, 10, city.getGasPrice()));

                // Scenario 2: Side-Hustle (25 hrs/week)
                scenarios.add(calculateScenario("Side-Hustle (25 hrs/wk)", tier.getSideHustleGross(),
                                250, 25, city.getGasPrice()));

                // Scenario 3: Full-time (40 hrs/week)
                scenarios.add(calculateScenario("Full-time (40 hrs/wk)", tier.getFullTimeGross(),
                                400, 40, city.getGasPrice()));

                return scenarios;
        }

        private CityScenario generateScenarioByWorkLevel(CityData city, String app, WorkLevel workLevel) {
                CityData.MarketTier tier = city.getMarketTier();

                return switch (workLevel) {
                        case PART_TIME -> calculateScenario(
                                        workLevel.getDisplayName() + " (" + workLevel.getHoursPerWeek() + " hrs/wk)",
                                        tier.getPartTimeGross(), workLevel.getMilesPerWeek(),
                                        workLevel.getHoursPerWeek(),
                                        city.getGasPrice());
                        case SIDE_HUSTLE -> calculateScenario(
                                        workLevel.getDisplayName() + " (" + workLevel.getHoursPerWeek() + " hrs/wk)",
                                        tier.getSideHustleGross(), workLevel.getMilesPerWeek(),
                                        workLevel.getHoursPerWeek(),
                                        city.getGasPrice());
                        case FULL_TIME -> calculateScenario(
                                        workLevel.getDisplayName() + " (" + workLevel.getHoursPerWeek() + " hrs/wk)",
                                        tier.getFullTimeGross(), workLevel.getMilesPerWeek(),
                                        workLevel.getHoursPerWeek(),
                                        city.getGasPrice());
                };
        }

        private CityScenario calculateScenario(String name, int gross, int miles, int hours, double gasPrice) {
                // Use IRS standard rate for simplicity (server-side calculation)
                double mileageDeduction = miles * AppConstants.IRS_MILEAGE_RATE_2024;
                double taxableProfit = gross - mileageDeduction;
                double taxes = taxableProfit * AppConstants.SELF_EMPLOYMENT_TAX_RATE;
                double netProfit = taxableProfit - taxes;
                double netHourly = netProfit / hours;

                return new CityScenario(name, gross, miles, hours, netProfit, netHourly);
        }
}
