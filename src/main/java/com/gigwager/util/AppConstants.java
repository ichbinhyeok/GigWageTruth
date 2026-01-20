package com.gigwager.util;

/**
 * AppConstants
 * 
 * Centralized source of truth for all business rates.
 * No magic numbers allowed in Service/Controller layers.
 */
public class AppConstants {

    // Financial Constants (2026)
    public static final double IRS_MILEAGE_RATE_2024 = 0.725; // 2026 IRS Standard Mileage Rate
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

    // Asset Versioning (Updates on every restart to bust cache)
    public static final String CACHE_VERSION = String.valueOf(System.currentTimeMillis());
}
