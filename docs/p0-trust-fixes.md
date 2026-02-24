# P0 Trust & YMYL Fixes

This document records the completion of all P0 requirements.

## P0-1: IRS 2026 Mileage Rate
- **Status:** **COMPLETE**
- **Changes:** Swept all JTE and Java files (including `AppConstants.java`, `WorkLevel.java`, `ProgrammaticSeoController.java`, `taxes.jte`, `cost-per-mile.jte`). Removed all outdated ("projected", "67-72.5", "2024") references. Fixed the global IRS rate to exactly `0.725` (72.5 cents per mile) as the 2026 IRS standard mileage rate. Added the official source link to `taxes.jte`.

## P0-2: 1099-K Threshold Statements
- **Status:** **COMPLETE**
- **Changes:** Removed the hardcoded `$600` 1099-K limit from `WorkLevel.java`. Replaced with safe YMYL language: "Reporting thresholds change. Regardless of forms received, income is taxable and must be reported. Check current IRS 1099-K guidance."

## P0-3: Remove "False Precision / Overclaims"
- **Status:** **COMPLETE**
- **Changes:** Updated meta descriptions in `ProgrammaticSeoController.java` to use "liquid hourly wage estimate" rather than "TRUE exact take-home". Replaced absolute insurance claims ("will deny the claim", "only way to avoid bankruptcy") in `rideshare-basics.jte` and `verdict_card.jte` with risk-framing ("may deny the claim depending on your policy"). Checked across `app-hub` and `best-cities` for appropriate disclaimers.

## P0-4: Fix Broken Tests
- **Status:** **COMPLETE**
- **Changes:** Fixed `PlaceholderLeakTest.java` to properly strip JSON-LD and style blocks without wiping the actual content, using `(?s)<.*?>` to remove structural HTML while keeping literal `[placeholder]` text exposed for the test string matcher. `SitemapXmlTest` was fixed resolving JUnit vs Java `assert` prior.

## P0-5: Sitemap Completeness
- **Status:** **COMPLETE**
- **Changes:** Updated `SitemapController` to automatically serve newly generated cluster pages (`/taxes/...`, `/insurance/...`, `/vehicle-cost/...`) as well as the newly built PR8 App Hubs (`/salary/uber`, `/best-cities/uber`, `/compare/...`) while maintaining proper indexing policy.
