package com.gigwager.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * AppConstants
 * 
 * Centralized source of truth for all business rates.
 * No magic numbers allowed in Service/Controller layers.
 */
public class AppConstants {

    // Financial Constants (2026)
    public static final double IRS_MILEAGE_RATE = 0.725; // 2026 IRS Standard Mileage Rate
    public static final double SELF_EMPLOYMENT_TAX_RATE = 0.153; // 15.3% (Social Security + Medicare)

    // Comparison Benchmarks
    public static final double MIN_WAGE_BURGER_KING = 16.00; // Example: CA Fast Food Min Wage is $20, but using
                                                             // national avg anchor
    public static final double MIN_WAGE_AMAZON = 17.00;

    // SEO & Meta
    public static final String SITE_NAME = "GigWageTruth";
    public static final String BASE_URL = "https://gigverdict.com";
    public static final String DEFAULT_DESCRIPTION = "Discover your TRUE hourly wage as a gig worker. Don't let gross revenue fool you.";
    public static final int CURRENT_YEAR = java.time.LocalDate.now().getYear();
    // Auto-generated from the latest content-affecting git commit at build time.
    // Override with env var SITEMAP_LASTMOD_DATE when needed (YYYY-MM-DD).
    public static final String SITEMAP_LASTMOD_DATE = resolveSitemapLastmodDate();

    // Asset Versioning (Updates on every restart to bust cache)
    public static final String CACHE_VERSION = String.valueOf(System.currentTimeMillis());

    private static String resolveSitemapLastmodDate() {
        String envOverride = System.getenv("SITEMAP_LASTMOD_DATE");
        if (isIsoDate(envOverride)) {
            return envOverride;
        }

        try (InputStream input = AppConstants.class.getClassLoader().getResourceAsStream("sitemap-lastmod.txt")) {
            if (input != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                    String line = reader.readLine();
                    if (isIsoDate(line)) {
                        return line.trim();
                    }
                }
            }
        } catch (Exception ignored) {
            // Fall through to stable runtime fallback below.
        }

        return LocalDate.now().toString();
    }

    private static boolean isIsoDate(String value) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        if (!trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }
        try {
            LocalDate.parse(trimmed);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
