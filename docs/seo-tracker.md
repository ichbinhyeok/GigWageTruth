# SEO Tracker

Last updated: 2026-03-20  
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
- Submit sitemap after deploy:
  - `https://gigverdict.com/sitemap.xml`
- Request manual indexing after deploy for:
  - `https://gigverdict.com/best-cities/doordash`
  - `https://gigverdict.com/best-cities/uber`
  - `https://gigverdict.com/salary/uber`
  - `https://gigverdict.com/uber/where-you-can-drive`
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

## Early-April Recheck Checklist
- Recheck window:
  - First pass: 2026-04-03
  - Second pass: 2026-04-10
- Indexing checks:
  - Confirm `/salary/uber` is no longer `Discovered - currently not indexed`
  - Confirm `/uber/where-you-can-drive` is no longer `URL is unknown to Google`
  - Confirm `/about` shows `noindex,follow` in live HTML and is absent from the live sitemap
- Query-fit checks:
  - Compare whether coverage-style Uber queries start shifting from `/best-cities/uber` to `/uber/where-you-can-drive`
  - Check whether `/best-cities/doordash` loses `availability` / `supported cities` style impressions
- CTR checks:
  - Re-measure sitewide CTR and US-only CTR
  - Re-measure `/best-cities/doordash`, `/best-cities/uber`, `/blog/multi-apping-guide`, and top `/compare/*` pages
  - Flag any page still above 150 impressions with 0 clicks
- Snippet checks:
  - Pull live SERPs for `/salary/uber`, `/best-cities/uber`, and one or two `/compare/*` pages to confirm the rewritten titles are actually being used
  - Re-open Search Console inspection for `/best-cities/doordash` and `/best-cities/uber` to see whether `Bad escape sequence in string` has cleared after recrawl
- Decision gate:
  - If `/salary/uber` and `/uber/where-you-can-drive` still are not properly indexed by 2026-04-10, treat discovery/indexing as the main blocker
  - If indexing is fixed but CTR is still near current levels, shift the next sprint toward authority/E-E-A-T strengthening and selective page pruning rather than more template expansion

## Review Log

### Review Entry
- Review date: 2026-03-20
- Current period: 2026-02-21 to 2026-03-19
- Comparison period: 2026-01-24 to 2026-02-20
- Site clicks / impressions / CTR / avg position: 19 / 24,671 / 0.077% / 6.53
- US-only clicks / impressions / CTR / avg position: 18 / 13,988 / 0.129% / 6.75
- Top 5 pages by impressions:
  - `/best-cities/doordash`: 1 click / 936 impressions / 0.11% CTR / avg pos 6.27
  - `/blog/multi-apping-guide`: 0 clicks / 518 impressions / 0.00% CTR / avg pos 8.36
  - `/best-cities/uber`: 0 clicks / 381 impressions / 0.00% CTR / avg pos 5.32
  - `/compare/denver/uber-vs-doordash`: 0 clicks / 273 impressions / 0.00% CTR / avg pos 3.22
  - `/salary/uber/orlando`: 1 click / 268 impressions / 0.37% CTR / avg pos 8.86
- Top 5 pages by clicks:
  - `/salary/uber/new-orleans/side-hustle`: 2 clicks / 7 impressions / 28.57% CTR / avg pos 6.14
  - `/best-cities/doordash`: 1 click / 936 impressions / 0.11% CTR / avg pos 6.27
  - `/`: 1 click / 251 impressions / 0.40% CTR / avg pos 3.67
  - `/salary/doordash/san-jose`: 1 click / 234 impressions / 0.43% CTR / avg pos 7.73
  - `/salary/uber/austin`: 1 click / 223 impressions / 0.45% CTR / avg pos 8.37
- High-impression zero-click pages:
  - `/blog/multi-apping-guide`: 518 impressions / 0 clicks / avg pos 8.36
  - `/best-cities/uber`: 381 impressions / 0 clicks / avg pos 5.32
  - `/compare/denver/uber-vs-doordash`: 273 impressions / 0 clicks / avg pos 3.22
  - `/about`: 156 impressions / 0 clicks / avg pos 2.91
  - `/compare/dallas/uber-vs-doordash`: 93 impressions / 0 clicks / avg pos 4.23
- Low-CTR queries worth action:
  - `doordash availability and performance by city 2026` -> `/best-cities/doordash`: 27 impressions / avg pos 1.85 / 0 clicks
  - `doordash official coverage areas or cities list 2026` -> `/best-cities/doordash`: 1 impression / avg pos 2.00 / 0 clicks
  - `doordash supported cities list 2026` -> `/best-cities/doordash`: 3 impressions / avg pos 6.33 / 0 clicks
  - `highest paying cities for uber drivers 2026` -> `/best-cities/uber`: 3 impressions / avg pos 3.00 / 0 clicks
- Query migration between related pages:
  - No visible migration yet from `/best-cities/uber` into `/uber/where-you-can-drive`.
  - `/uber/where-you-can-drive` returned zero query rows in the current period.
- SEO CTA event notes:
  - GA4 property access is still pending, so live CTA event quality cannot be verified from analytics yet.
- URL inspection notes:
  - `/` is `Submitted and indexed` and was crawled on 2026-03-11.
  - `/best-cities/doordash` is `Submitted and indexed`, but the last crawl in inspection is still 2026-02-27.
  - `/best-cities/uber` is `Submitted and indexed`, but the last crawl in inspection is still 2026-02-25.
  - `/salary/doordash` is `Submitted and indexed` with last crawl 2026-02-25.
  - `/salary/uber` is `Discovered - currently not indexed`.
  - `/uber/where-you-can-drive` is live with HTTP 200 and present in `sitemap.xml`, but inspection still says `URL is unknown to Google`.
- Structured-data status:
  - `/best-cities/doordash` and `/best-cities/uber` still show `Unparsable structured data` with `Bad escape sequence in string` in Search Console inspection.
  - Because the indexed crawls on both pages predate the 2026-03-11 deploy, this still looks like a recrawl/refresh issue rather than proof that the live HTML is broken.
- Cannibalization notes:
  - No meaningful cannibalization was detected in the current 28-day window.
- Changes shipped since last review:
  - Live sitemap and page metadata confirm the 2026-03-11 release is deployed, including `/uber/where-you-can-drive` and the rewritten `/best-cities/{app}` titles/descriptions.
- Changes still pending deploy:
  - Rewrote `/blog/multi-apping-guide` to remove visible text corruption and align the page around `clean stacking` / deactivation-safe intent.
  - Tightened `/compare/{city}/uber-vs-doordash` titles, descriptions, and hero copy so the winning app and hourly gap are visible before the click.
  - Added stronger homepage links into `/salary/uber`, `/salary/doordash`, `/uber/where-you-can-drive`, and the clean-stacking guide to improve crawl paths from an already indexed page.
  - Rebuilt `/salary/{app}` so the hub is now side-hustle aligned, richer in explanatory content, and ships `BreadcrumbList` + `ItemList` + `FAQPage` JSON-LD instead of a thin top-10 table.
  - Marked `/about` as `noindex,follow` and removed it from `sitemap.xml` so a zero-click trust page stops consuming impression budget.
  - Fixed the shared related-pages component so anchors and destinations now match, and added the missing `/best-cities/{app}` link path.
  - Updated the blog index card for `/blog/multi-apping-guide` so the listing matches the rewritten clean-stacking article and no longer shows broken separator text.
  - Reduced layout blocking a bit by making the Inter stylesheet non-blocking and by stubbing `gtag()` before lazy analytics boot so early clicks do not throw client-side errors.
- Local verification after patch:
  - `.\gradlew test --console=plain`: pass
  - `OrganicMonitoringRegressionTest`: pass
  - `build/reports/organic-monitoring-report.json`: `failures = 0`
  - Remaining warning after the latest local pass: `/salary/uber` meta description is still slightly long at 186 characters
- Recrawl requested for:
  - `https://gigverdict.com/best-cities/doordash`
  - `https://gigverdict.com/best-cities/uber`
  - `https://gigverdict.com/salary/uber`
  - `https://gigverdict.com/uber/where-you-can-drive`
- Hypothesis for next cycle:
  - CTR is still being suppressed by a mix of intent leakage on list/comparison pages and stale Google crawls on the pages that were updated on 2026-03-11.
  - `/salary/uber` not being indexed is likely reducing discovery for the new Uber coverage page.
  - `/blog/multi-apping-guide` is still accumulating zero-click impressions while the live template contains visible text corruption, so trust damage may still be affecting click behavior on that article.
- Decision / next action:
  - Deploy the pending patch set first.
  - Submit `https://gigverdict.com/sitemap.xml` in Search Console right after deploy.
  - Request fresh indexing for the four URLs above inside Search Console immediately after deploy.
  - Verify that `/about` returns `noindex,follow` in live HTML and is absent from the live sitemap.
  - If `/uber/where-you-can-drive` is still unknown at the next review, add one stronger internal link from the homepage or the main Uber hub block and consider linking it from another indexed page with recent crawl activity.
  - If `/compare/*` pages keep sitting in positions 3-5 with zero clicks, rewrite titles/heroes to surface a concrete city-specific winner or net-pay delta instead of the generic `Who Nets More?` framing.

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
