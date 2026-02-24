# Phase 2: Organic Expansion (Intent Hubs & Long-Tail Logic)

## Objective
To massively increase our "hooks" in long-tail search by addressing deeply specific, high-intent queries that static template pages cannot effectively capture. We aimed to create authoritative, "answer-first" hub pages that capture queries like "Uber after expenses" or "how to calculate net hourly pay."

## What Was Implemented

### 1. New Intent Hubs (Topical Authority)
Four new dedicated hub landing pages were built to capture specific head terms:
- **/uber-after-expenses**: Breaks down the specific cost deductions for Uber.
- **/doordash-after-expenses**: Explains wear and tear and wait-time specific to DoorDash.
- **/net-hourly-calculator**: A high-level hub that educates drivers on why "active time" represents fake earnings.
- **/multi-apping**: Captures "how to multi-app," redirecting organic traffic into the calculator ecosystem.

### 2. Answer-Engine Optimization (AEO) Upgrades
We updated the existing programmatic SEO templates (`compare.jte` and `best-cities.jte`) to serve bots and AI search better:
- **TL;DR "Answer-First" Blocks**: Placed prominently to answer queries directly.
- **Why They Win Logic**: On the Best Cities pages, we pull in real JSON data (surge areas, nightlife districts) to provide hyper-local, realistic reasoning for *why* a city is ranked high.
- **Method & Weights Disclosure**: Build algorithmic trust by explaining *how* the site ranks cities.

### 3. Spider-Web Internal Linking (No Orphan Pages)
- Added `@template.components.related_pages(app = app)` to automatically interlink hub pages, calculators, and blogs contextually.
- Added `@template.components.next_steps_by_worklevel(hoursPerWeek = hours)` to dynamically trigger hyper-relevant articles based on the simulated work profile. For instance, high hours suggest the quarterly tax estimator constraint.

## Status
- **Status**: Completed.
- **URL Routes**: Verified in `PageController` and `SitemapController`.
- **Rank Readiness**: High. These hubs act as trust anchors for the programmatic SEO cluster.
