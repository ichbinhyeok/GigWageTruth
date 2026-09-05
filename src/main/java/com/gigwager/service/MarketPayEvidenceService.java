package com.gigwager.service;

import com.gigwager.model.MarketPayEvidence;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MarketPayEvidenceService {

    private static final Map<String, List<MarketPayEvidence>> EVIDENCE = Map.ofEntries(
            Map.entry("uber/nashville", List.of(
                    evidence("Official", "Uber", 25.83, "Median earnings", "Nashville",
                            "Trip fares, certain promotions, and tips; drivers are paid per completed trip, not hourly.",
                            "Aug 3-31, 2026", "https://www.uber.com/us/en/e/drive/nashville-tn-us/"),
                    evidence("Tracked", "Solo", 23.18, "Tracked total pay", "Nashville",
                            "Base pay, bonus pay, and tips tracked in the Solo app; expenses are not identified as deducted.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/uber-nashville-tn"))),
            Map.entry("uber/los-angeles", List.of(
                    evidence("Tracked", "Solo", 23.73, "Tracked total pay", "Los Angeles / Orange County",
                            "Base pay, bonus pay, and tips tracked in the Solo app; broader than Los Angeles city limits.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/uber-los-angeles-orange-county-ca"),
                    evidence("Independent", "Indeed", 24.17, "Posted salary estimate", "Los Angeles",
                            "Estimate from five past and present job postings; vehicle expenses are not identified as deducted.",
                            "May 14, 2026", "https://www.indeed.com/cmp/Uber/salaries/Driver/Los-Angeles-CA"))),
            Map.entry("uber/chicago", List.of(
                    evidence("Tracked", "Solo", 24.89, "Tracked total pay", "Chicago",
                            "Base pay, bonus pay, and tips tracked in the Solo app; expenses are not identified as deducted.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/uber-chicago-il"),
                    evidence("Independent", "UC Berkeley IRLE", 13.93, "Adjusted net pay", "Chicago",
                            "2024 Uber data adjusted for full working time, expenses, and tips by an independent labor study.",
                            "Jan 14, 2026", "https://irle.berkeley.edu/publications/irle-policy-brief/rideshare-driver-pay-in-chicago-philadelphia-and-portland/"))),
            Map.entry("uber/austin", List.of(
                    evidence("Tracked", "Solo", 22.95, "Tracked total pay", "Austin",
                            "Base pay, bonus pay, and tips tracked in the Solo app; expenses are not identified as deducted.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/uber-austin-tx"))),
            Map.entry("doordash/los-angeles", List.of(
                    evidence("Tracked", "Solo", 14.64, "Tracked total pay", "Los Angeles / Orange County",
                            "Base pay and tips tracked in the Solo app; broader than Los Angeles city limits and before any stated expense deduction.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/doordash-los-angeles-orange-county-ca"))),
            Map.entry("doordash/phoenix", List.of(
                    evidence("Tracked", "Solo", 16.82, "Tracked total pay", "Phoenix",
                            "Base pay and tips tracked in the Solo app; expenses are not identified as deducted.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/doordash-phoenix-az"))),
            Map.entry("doordash/atlanta", List.of(
                    evidence("Tracked", "Solo", 14.47, "Tracked total pay", "Atlanta",
                            "Base pay and tips tracked in the Solo app; expenses are not identified as deducted.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/doordash-atlanta-ga"))),
            Map.entry("doordash/boston", List.of(
                    evidence("Tracked", "Solo", 18.11, "Tracked total pay", "Boston",
                            "Base pay and tips tracked in the Solo app; expenses are not identified as deducted.",
                            "Sep 2, 2026", "https://www.worksolo.com/pay-insights/doordash-boston-ma"))));

    public List<MarketPayEvidence> getEvidence(String app, String citySlug) {
        if (app == null || citySlug == null) {
            return List.of();
        }
        return EVIDENCE.getOrDefault(app + "/" + citySlug, List.of());
    }

    private static MarketPayEvidence evidence(
            String evidenceType,
            String sourceName,
            double hourlyPay,
            String payLabel,
            String marketCoverage,
            String definition,
            String observedAt,
            String sourceUrl) {
        return new MarketPayEvidence(evidenceType, sourceName, hourlyPay, payLabel, marketCoverage,
                definition, observedAt, sourceUrl);
    }
}
