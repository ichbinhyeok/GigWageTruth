# SEO Tracker

Last updated: 2026-03-11  
Owner: GigWageTruth  
Primary source: Google Search Console (GA4 property access still pending)

## Current Verdict
- Indexing and canonical health: mostly good
- Visibility (impressions/rank): accelerating
- Click conversion (CTR/clicks): improving, but still weak for current ranking levels
- Primary bottleneck: query intent mismatch on list/ranking pages, not raw ranking

## Current Snapshot
- Current period: 2026-02-12 to 2026-03-10
- Previous period: 2026-01-15 to 2026-02-11
- Clicks: 2 -> 12 (+500%)
- Impressions: 3,342 -> 14,049 (+320.4%)
- CTR: 0.060% -> 0.085% (+42.7%)
- Avg position: 10.25 -> 6.16 (improved)

## Market Split Note
- US only (last 28 days): 11 clicks / 7,985 impressions / 0.138% CTR / avg pos 6.48
- Sitewide CTR is lower than US-only CTR because non-US impressions are diluting the aggregate number.

## Top Page Watchlist (last 28 days by impressions)
- `/best-cities/doordash`: 1 click / 384 impressions / 0.26% CTR / avg pos 5.86
- `/`: 1 click / 230 impressions / 0.43% CTR / avg pos 2.92
- `/best-cities/uber`: 0 clicks / 206 impressions / 0.00% CTR / avg pos 4.65
- `/about`: 0 clicks / 153 impressions / 0.00% CTR / avg pos 2.91
- `/salary/doordash/minneapolis`: 1 click / 110 impressions / 0.91% CTR / avg pos 5.82

## Key Observations (2026-03-11)
- Ranking is no longer the primary bottleneck. Average position moved from 10.25 to 6.16 while clicks also rose, but CTR is still far below what pages in positions 3-8 should usually earn.
- `/best-cities/doordash` is attracting the wrong kind of impression. Search Console low-CTR data showed the query `doordash availability and performance by city 2026` at avg pos 1.78 with 23 impressions and 0 clicks. This confirms list-page intent leakage.
- `/best-cities/uber` is likely leaking the same type of coverage/support-city intent even before clicks materialize. The page had 206 impressions, 0 clicks, and avg position 4.65. Treat this as the first page to watch for intent migration after a dedicated coverage page ships.
- Sampled URL inspections for `/`, `/best-cities/doordash`, `/best-cities/uber`, `/salary/doordash/minneapolis`, and `/salary/uber/columbus/side-hustle` all returned `Submitted and indexed`. Indexing is not the blocker.
- Search Console URL Inspection still reports `Bad escape sequence in string` on sampled rich-result pages, but current live HTML JSON-LD parsed cleanly when fetched directly. Treat this as a recrawl/inspection follow-up item until Google refreshes the rendered version.
- Search appearance data is still effectively empty. Do not assume rich-result eligibility is helping click-through yet.
- Cannibalization check returned no meaningful conflicts in the current 28-day window.
- Sitemap API output showed `submitted 260 / indexed 0`, but sampled URL inspections contradicted that. Treat the sitemap indexed count as noisy until rechecked.

## Changes Prepared In Code On 2026-03-11
- Repositioned `/best-cities/{app}` metadata from generic `best cities` phrasing to `highest-paying cities` / `net earnings ranking`.
- Added an explicit intent note on `/best-cities/{app}` clarifying that the page is an earnings ranking, not an official coverage list.
- Tightened the above-the-fold copy on `/best-cities/{app}` to match the earnings-ranking promise.
- Added a new intent-split landing page at `/uber/where-you-can-drive` for coverage/support-city searches.
- Built the new Uber page around a clear two-step promise:
  - official Uber city directory for current availability
  - GigVerdict city reports for pay-after-expenses analysis
- Added internal links from `/salary/uber` and `/best-cities/uber` into the new coverage-intent page so search and navigation can separate `coverage` from `earnings ranking`.
- Added tracked CTA labels on the key Uber intent-split pages:
  - `/salary/uber`
  - `/uber/where-you-can-drive`
- Added an above-the-fold action block on city report pages that links directly to:
  - the app-specific calculator with prefilled gross / miles / hours / gas price
  - the quarterly tax estimator
  - the highest-paying cities ranking
- Added sitemap coverage and regression-test coverage for `/uber/where-you-can-drive`.
- Added regression coverage to ensure city report hero actions and coverage-page CTA tracking stay rendered.
- Cleaned visible text corruption on:
  - homepage (`index.jte`)
  - about page (`about.jte`)
  - app hub page (`salary/app-hub.jte`)
  - verdict card component (`components/verdict_card.jte`)

## Validation Result
- `EncodingCorruptionGuardTest`: pass
- `OrganicMonitoringRegressionTest`: pass
- Note: one non-clean incremental build produced a transient Gradle/JTE compile-state issue; `clean test` resolved it and the final verification passed.

## What We Are Testing Now
- Hypothesis 1: narrowing `/best-cities/{app}` to explicit earnings intent will reduce low-quality impressions and improve CTR.
- Hypothesis 2: removing visible text corruption will improve trust and reduce click waste / bounce on brand-adjacent and entry pages.
- Hypothesis 3: once Google recrawls the updated pages, structured-data inspection errors should clear if the current JSON-LD serialization is truly fixed.
- Hypothesis 4: a dedicated Uber coverage-intent page will absorb support-city / availability queries that previously leaked into `/best-cities/uber`, improving query-to-page fit.
- Hypothesis 5: if query-to-page fit improves but clicks still do not turn into product behavior, the new tracked hero CTAs will show whether the landing experience is failing to move users into calculators and tax tools.

## 14-Day Success Criteria
- Site CTR >= 0.10%
- At least 3 top-impression pages with non-zero clicks
- `/best-cities/doordash` CTR meaningfully above 0.26%
- `/best-cities/uber` CTR meaningfully above 0.00%
- `/uber/where-you-can-drive` begins receiving impressions for coverage-style Uber queries
- Once GA4 access is connected, new `cta_click` labels should begin appearing for:
  - `open_official_uber_directory`
  - `open_uber_pay_reports`
  - `open_prefilled_calculator`
  - `estimate_quarterly_taxes`
- No sampled URL inspection result showing fresh `Bad escape sequence` after recrawl
- No visible text corruption on key landing pages

## Immediate Follow-Up Actions
- Deploy current code changes.
- Re-run URL Inspection after deployment for:
  - `https://gigverdict.com/best-cities/doordash`
  - `https://gigverdict.com/best-cities/uber`
  - `https://gigverdict.com/uber/where-you-can-drive`
  - `https://gigverdict.com/salary/doordash/minneapolis`
  - `https://gigverdict.com/salary/uber/columbus/side-hustle`
  - `https://gigverdict.com/`
- Compare 7-day and 28-day CTR deltas after recrawl.
- Compare query migration between:
  - `/best-cities/uber`
  - `/uber/where-you-can-drive`
- Watch for Uber query patterns such as `uber cities`, `where can i drive for uber`, `uber available cities`, and `uber driver cities` to see whether the new page is matching the right intent.
- Connect GA4 property access so landing-page engagement and CTA events can be tied back to SEO traffic quality.
- Once GA4 access is available, verify the new event labels fire on live pages:
  - `open_official_uber_directory`
  - `open_uber_pay_reports`
  - `open_prefilled_calculator`
  - `estimate_quarterly_taxes`

## Follow-Up Entry Template
Copy this block for each new review cycle.

### Review Entry
- Review date:
- Current period:
- Comparison period:
- Site clicks / impressions / CTR / avg position:
- US-only clicks / impressions / CTR / avg position:
- Top 5 pages by impressions:
- Top 5 pages by clicks:
- High-impression zero-click pages:
- Low-CTR queries worth action:
- Query migration between related pages:
- SEO CTA event notes:
- URL inspection notes:
- Structured-data status:
- Cannibalization notes:
- Changes shipped since last review:
- Changes still pending deploy:
- Recrawl requested for:
- Hypothesis for next cycle:
- Decision / next action:

## Operating Rules
- Do not judge CTR off sitewide averages alone. Always compare sitewide and US-only views.
- For list pages, record both page-level CTR and the top leaking query intents.
- Do not mark structured-data work as complete until URL Inspection reflects the new rendered version.
- If a page keeps generating impressions with near-zero clicks for two consecutive reviews, either retarget its intent or reduce its indexing priority.
