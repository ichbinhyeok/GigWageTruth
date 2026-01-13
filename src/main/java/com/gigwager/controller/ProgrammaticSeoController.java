package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;
import com.gigwager.model.SeoMeta;
import com.gigwager.util.AppConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProgrammaticSeoController {

        @GetMapping("/salary/{app}/{citySlug}")
        public String citySalaryPage(@PathVariable String app,
                        @PathVariable String citySlug,
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
                String title = String.format("Real %s Earnings in %s, %s (2026 Truth)",
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

                String canonicalUrl = String.format("https://www.gigwagetruth.com/salary/%s/%s", app, citySlug);

                // Freshness signal
                String lastUpdated = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")
                                .format(java.time.LocalDate.now());

                model.addAttribute("app", app);
                model.addAttribute("appName", appName);
                model.addAttribute("city", city);
                model.addAttribute("scenarios", scenarios);
                model.addAttribute("featuredScenario", featuredScenario);
                model.addAttribute("lastUpdated", lastUpdated);
                model.addAttribute("seoMeta", new SeoMeta(title, description, canonicalUrl,
                                "https://www.gigwagetruth.com/og-image.jpg"));

                return "salary/city-report";
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
