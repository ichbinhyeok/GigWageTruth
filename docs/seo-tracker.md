# SEO Tracker

Last updated: 2026-03-22  
Owner: GigWageTruth  
Primary source: Google Search Console (GA4 property access still pending)

## Current Verdict
- Indexing and canonical health: mostly good
- Visibility (impressions/rank): still strong versus the prior 28-day window
- Click conversion (CTR/clicks): still weak for pages already ranking in positions 3-8
- Primary bottleneck: low click conversion on `salary-city` pages, with secondary indexing drag on `/salary/uber`

## Current Snapshot
- Current period: 2026-02-21 to 2026-03-20
- Previous period: 2026-01-24 to 2026-02-20
- Clicks: 0 -> 24
- Impressions: 2,592 -> 28,337 (+993.2%)
- CTR: 0.000% -> 0.085%
- Avg position: 9.91 -> 6.66 (improved)

## Market Split Note
- US only (last 28 days): 23 clicks / 16,528 impressions / 0.139% CTR / avg pos 6.90
- Sitewide CTR is lower than US-only CTR because non-US impressions are diluting the aggregate number.
- Device split (last 28 days): mobile `19 / 12,824 / 0.148% CTR / avg pos 6.51`, desktop `4 / 15,414 / 0.026% CTR / avg pos 6.77`

## Top Page Watchlist (last 28 days by impressions)
- `/salary/doordash/denver/side-hustle`: 0 clicks / 3,550 impressions / 0.00% CTR / avg pos 3.87
- `/salary/doordash/phoenix`: 0 clicks / 1,309 impressions / 0.00% CTR / avg pos 3.79
- `/best-cities/doordash`: 1 click / 1,072 impressions / 0.09% CTR / avg pos 6.31
- `/salary/directory`: 0 clicks / 814 impressions / 0.00% CTR / avg pos 3.67
- `/salary/doordash/denver`: 0 clicks / 739 impressions / 0.00% CTR / avg pos 4.81

## Key Observations (2026-03-22)
- Ranking is no longer the primary bottleneck. The biggest current CTR hole is the `salary-city` cluster, not the ranking pages alone.
- `salary-city` group totals: `14 clicks / 28,682 impressions / 0.049% CTR / avg pos 6.55`. This is materially worse than the `compare` cluster, which produced `7 clicks / 1,532 impressions / 0.457% CTR / avg pos 6.03`.
- `/salary/doordash/denver/side-hustle` and `/salary/doordash/phoenix` are now the clearest CTR failures. Both are ranking around positions 3-4 with zero clicks, which means snippet/message fit is the main problem.
- `/best-cities/doordash` still shows intent leakage. Search Console again exposed `doordash availability and performance by city 2026` with `27 impressions / avg pos 1.85 / 0 clicks`.
- `/best-cities/uber` is no longer the only priority CTR issue. It is still weak, but the larger traffic waste is happening on city pages and `/salary/directory`.
- `/uber/where-you-can-drive` is now `Submitted and indexed` with a `2026-03-20` crawl. This page should no longer be treated as an unknown-URL indexing blocker.
- `/salary/uber` is still `Discovered - currently not indexed`. This remains the main indexing issue in the Uber cluster.
- `/about` now returns `noindex,follow` in live HTML and is absent from the live sitemap. Search Console still shows it indexed because the recrawl has not caught up yet.
- Search Console URL Inspection still reports `Bad escape sequence in string` on sampled rich-result pages such as `/best-cities/doordash`, `/best-cities/uber`, and `/salary/doordash/denver/side-hustle`, but the current live JSON-LD rendered cleanly in local validation. Treat this as a stale-crawl follow-up item until Google refreshes the rendered version.
- Search appearance data is still effectively empty. Do not assume rich-result eligibility is contributing to click-through yet.
- Sitemap API still reports `submitted 260 / indexed 0`, but direct URL inspection contradicts that. Keep treating the sitemap indexed count as noisy.

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

## Changes Prepared In Code On 2026-03-22
- Rewrote metadata across the main organic templates so titles now align around `earnings` / `after expenses` instead of generic `pay` phrasing:
  - `/salary/{app}`
  - `/salary/{app}/{city}`
  - `/salary/{app}/{city}/{workLevel}`
  - `/compare/{city}/uber-vs-doordash`
  - `/salary/directory`
  - `/blog/multi-apping-guide`
- Tightened title lengths so `OrganicMonitoringRegressionTest` no longer fails on long titles for high-priority landing pages.
- Added an explicit intent block to `/salary/{app}` clarifying the difference between:
  - city reports
  - ranking page
  - calculator
  - coverage guide
- Added a dedicated coverage-intent card to `/salary/uber` so the hub better separates `earnings` from `coverage`.
- Re-aligned `/compare/{city}/uber-vs-doordash` H1 with the updated metadata so the page headline now matches the comparison promise visible in search.
- Re-aligned `/salary/{app}/{city}` and `/salary/{app}/{city}/{workLevel}` H1s with the new `after expenses` framing to improve title/H1 consistency.
- Strengthened `/best-cities/{app}` above-the-fold copy so ranking intent is explicit before the user reaches the disclosure block.
- Rewrote `/salary/directory` metadata and hero copy around `Uber and DoorDash earnings by city` and fixed the broken button text labels for:
  - `Uber`
  - `DoorDash`
- Repositioned `/blog/multi-apping-guide` around the more literal query intent `How to multi-app Uber and DoorDash without getting deactivated`.
- Rebalanced the homepage, shared toolkit links, top navigation, and blog CTA paths so the site now pushes users toward:
  - calculators
  - quarterly tax estimator
  - cost-per-mile tool
  - coverage guide
  - decision-first blog content
- Reduced the prominence of `salary-city` and directory-first paths in the first-click experience so the pivot can test whether stronger decision intent improves clicks and downstream tool usage.
- Rewrote blog index card headlines and CTA copy to match more literal user questions instead of abstract editorial phrasing.
- Reframed the highest-priority `salary-city` titles and descriptions around the more direct question `Is {app} worth it in {city}?` so high-impression city pages carry a stronger click hook.
- Added compare-path links in the highest-value templates so users and crawlers can move from:
  - app hub -> compare page
  - city page -> compare page
  - directory -> compare page
- Reworked `/salary/uber` hub copy so it is more distinct from the ranking page and directory, with a clearer role for:
  - city reports
  - compare pages
  - calculator
  - coverage guide
- Adjusted the mobile home-page order so the calculator appears before the long brand narrative, reducing friction for calculator-first usage and stabilizing the E2E flow that mirrors that path.

## Validation Result
- `EncodingCorruptionGuardTest`: pass
- `OrganicMonitoringRegressionTest`: pass
- `.\gradlew.bat test --no-daemon`: pass
- `build/reports/organic-monitoring-report.json`: `failures = 0`
- `build/reports/organic-monitoring-report.json`: `warnings = 0`

## What We Are Testing Now
- Hypothesis 1: narrowing `/best-cities/{app}` to explicit earnings intent will reduce low-quality impressions and improve CTR.
- Hypothesis 2: removing visible text corruption will improve trust and reduce click waste / bounce on brand-adjacent and entry pages.
- Hypothesis 3: once Google recrawls the updated pages, structured-data inspection errors should clear if the current JSON-LD serialization is truly fixed.
- Hypothesis 4: a dedicated Uber coverage-intent page will absorb support-city / availability queries that previously leaked into `/best-cities/uber`, improving query-to-page fit.
- Hypothesis 5: if query-to-page fit improves but clicks still do not turn into product behavior, the new tracked hero CTAs will show whether the landing experience is failing to move users into calculators and tax tools.
- Hypothesis 6: the biggest near-term CTR lift will come from city-report and compare snippets that better match `earnings after expenses` intent, not from additional content expansion.

## 14-Day Success Criteria
- Site CTR >= 0.10%
- At least 3 top-impression pages with non-zero clicks
- `/best-cities/doordash` CTR meaningfully above 0.26%
- `/best-cities/uber` CTR meaningfully above 0.00%
- `/uber/where-you-can-drive` begins receiving impressions for coverage-style Uber queries
- At least 2 current zero-click city pages above 500 impressions begin receiving clicks after snippet refresh
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
  - `https://gigverdict.com/salary/directory`
  - `https://gigverdict.com/blog/multi-apping-guide`
- Re-run URL Inspection after deployment for:
  - `https://gigverdict.com/best-cities/doordash`
  - `https://gigverdict.com/best-cities/uber`
  - `https://gigverdict.com/salary/uber`
  - `https://gigverdict.com/salary/directory`
  - `https://gigverdict.com/blog/multi-apping-guide`
  - `https://gigverdict.com/`
- Compare 7-day and 28-day CTR deltas after recrawl, especially on:
  - `/salary/doordash/denver/side-hustle`
  - `/salary/doordash/phoenix`
  - `/salary/directory`
  - `/best-cities/doordash`
  - `/best-cities/uber`
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
  - Confirm `/about` shows `noindex,follow` in live HTML and is absent from the live sitemap
- Coverage checks:
  - Confirm `/uber/where-you-can-drive` stays `Submitted and indexed` and begins collecting coverage-style impressions
- Query-fit checks:
  - Compare whether coverage-style Uber queries start shifting from `/best-cities/uber` to `/uber/where-you-can-drive`
  - Check whether `/best-cities/doordash` loses `availability` / `supported cities` style impressions
- CTR checks:
  - Re-measure sitewide CTR, US-only CTR, and desktop vs mobile CTR
  - Re-measure `/best-cities/doordash`, `/best-cities/uber`, `/blog/multi-apping-guide`, `/salary/directory`, and top `/compare/*` pages
  - Re-measure the current high-impression zero-click city pages, especially `/salary/doordash/denver/side-hustle` and `/salary/doordash/phoenix`
  - Flag any page still above 150 impressions with 0 clicks
- Snippet checks:
  - Pull live SERPs for `/salary/uber`, `/best-cities/uber`, and one or two `/compare/*` pages to confirm the rewritten titles are actually being used
  - Re-open Search Console inspection for `/best-cities/doordash` and `/best-cities/uber` to see whether `Bad escape sequence in string` has cleared after recrawl
- Decision gate:
  - If `/salary/uber` and `/uber/where-you-can-drive` still are not properly indexed by 2026-04-10, treat discovery/indexing as the main blocker
  - If indexing is fixed but CTR is still near current levels, shift the next sprint toward authority/E-E-A-T strengthening and selective page pruning rather than more template expansion

## Review Log

### Review Entry
- Review date: 2026-03-22
- Current period: 2026-02-21 to 2026-03-20
- Comparison period: 2026-01-24 to 2026-02-20
- Site clicks / impressions / CTR / avg position: 24 / 28,337 / 0.085% / 6.66
- US-only clicks / impressions / CTR / avg position: 23 / 16,528 / 0.139% / 6.90
- Device split:
  - mobile: 19 / 12,824 / 0.148% / 6.51
  - desktop: 4 / 15,414 / 0.026% / 6.77
- Top 5 pages by impressions:
  - `/salary/doordash/denver/side-hustle`: 0 clicks / 3,550 impressions / 0.00% CTR / avg pos 3.87
  - `/salary/doordash/phoenix`: 0 clicks / 1,309 impressions / 0.00% CTR / avg pos 3.79
  - `/best-cities/doordash`: 1 click / 1,072 impressions / 0.09% CTR / avg pos 6.31
  - `/salary/directory`: 0 clicks / 814 impressions / 0.00% CTR / avg pos 3.67
  - `/salary/doordash/denver`: 0 clicks / 739 impressions / 0.00% CTR / avg pos 4.81
- Top 5 pages by clicks:
  - `/compare/austin/uber-vs-doordash`: 2 clicks / 65 impressions / 3.08% CTR / avg pos 6.88
  - `/salary/uber/new-orleans/side-hustle`: 2 clicks / 10 impressions / 20.00% CTR / avg pos 5.20
  - `/`: 1 click / 257 impressions / 0.39% CTR / avg pos 3.65
  - `/best-cities/doordash`: 1 click / 1,072 impressions / 0.09% CTR / avg pos 6.31
  - `/salary/doordash/minneapolis`: 1 click / 216 impressions / 0.46% CTR / avg pos 6.20
- High-impression zero-click pages:
  - `/salary/doordash/denver/side-hustle`: 3,550 impressions / 0 clicks / avg pos 3.87
  - `/salary/doordash/phoenix`: 1,309 impressions / 0 clicks / avg pos 3.79
  - `/salary/directory`: 814 impressions / 0 clicks / avg pos 3.67
  - `/salary/doordash/denver`: 739 impressions / 0 clicks / avg pos 4.81
  - `/blog/multi-apping-guide`: 613 impressions / 0 clicks / avg pos 8.38
- Low-CTR queries worth action:
  - `doordash availability and performance by city 2026` -> `/best-cities/doordash`: 27 impressions / avg pos 1.85 / 0 clicks
  - `doordash official coverage areas or cities list 2026` -> `/best-cities/doordash`: 1 impression / avg pos 2.00 / 0 clicks
  - `doordash supported cities list 2026` -> `/best-cities/doordash`: 3 impressions / avg pos 6.33 / 0 clicks
  - `highest paying cities for uber drivers 2026` -> `/best-cities/uber`: 3 impressions / avg pos 3.00 / 0 clicks
- Query migration between related pages:
  - No meaningful migration is visible yet from `/best-cities/uber` into `/uber/where-you-can-drive`.
  - `/uber/where-you-can-drive` has now started receiving at least initial coverage-style impressions and is indexed.
- SEO CTA event notes:
  - GA4 property access is still pending, so live CTA event quality cannot be verified from analytics yet.
- URL inspection notes:
  - `/` is `Submitted and indexed` and was crawled on 2026-03-20.
  - `/best-cities/doordash` is `Submitted and indexed`, but the last crawl in inspection is still 2026-02-27.
  - `/best-cities/uber` is `Submitted and indexed`, but the last crawl in inspection is still 2026-02-25.
  - `/salary/uber` is still `Discovered - currently not indexed`.
  - `/uber/where-you-can-drive` is now `Submitted and indexed` with last crawl 2026-03-20.
  - `/about` still shows as indexed in Search Console, but live HTML already returns `noindex,follow` and the live sitemap no longer contains the URL.
- Structured-data status:
  - `/best-cities/doordash`, `/best-cities/uber`, and sampled rich city pages still show `Unparsable structured data` with `Bad escape sequence in string` in Search Console inspection.
  - Because the indexed crawls on those pages predate the newest live changes, this still looks like a recrawl/refresh issue rather than proof that the current HTML is broken.
- Cannibalization notes:
  - No meaningful cannibalization was detected in the current 28-day window outside of trivial site-operator noise.
- Changes shipped since last review:
  - Live Search Console data now confirms `/uber/where-you-can-drive` is indexed.
  - `/about` now behaves as intended on the live site: `noindex,follow` and removed from the live sitemap.
- Changes still pending deploy:
  - Rewrote metadata and title/H1 alignment across `/salary/{app}`, `/salary/{app}/{city}`, `/salary/{app}/{city}/{workLevel}`, `/compare/{city}/uber-vs-doordash`, `/salary/directory`, and `/blog/multi-apping-guide`.
  - Added an explicit intent block and coverage-intent card to `/salary/uber`.
  - Reframed `/salary/directory` around `Uber and DoorDash earnings by city` and fixed the broken button labels.
  - Repositioned `/blog/multi-apping-guide` around the literal query intent `How to multi-app Uber and DoorDash without getting deactivated`.
- Local verification after patch:
  - `.\gradlew.bat test --no-daemon`: pass
  - `OrganicMonitoringRegressionTest`: pass
  - `build/reports/organic-monitoring-report.json`: `failures = 0`
  - Remaining warning after the latest local pass: `/best-cities/uber` title may still truncate at 71 characters
- Recrawl requested for:
  - `https://gigverdict.com/best-cities/doordash`
  - `https://gigverdict.com/best-cities/uber`
  - `https://gigverdict.com/salary/uber`
  - `https://gigverdict.com/salary/directory`
  - `https://gigverdict.com/blog/multi-apping-guide`
- Hypothesis for next cycle:
  - CTR is now being suppressed more by weak snippet/message fit on city pages than by ranking.
  - `/salary/uber` not being indexed is still reducing discovery efficiency for the Uber hub cluster.
  - If the rewritten snippets get recrawled, the first CTR lift should appear on current zero-click city pages and compare pages before it appears on the full site aggregate.
- Decision / next action:
  - Deploy the current patch set.
  - Submit `https://gigverdict.com/sitemap.xml` in Search Console right after deploy.
  - Request fresh indexing for the five URLs above.
  - Re-open Search Console inspection for `/best-cities/doordash`, `/best-cities/uber`, `/salary/uber`, `/salary/directory`, and `/blog/multi-apping-guide`.
  - If `/salary/directory` remains above 500 impressions with zero clicks after recrawl, consider reducing its indexing priority rather than keeping it as a primary entry page.
  - If `/salary/uber` remains `Discovered - currently not indexed` at the next review, treat hub differentiation or crawl prioritization as the next blocker to solve.

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
