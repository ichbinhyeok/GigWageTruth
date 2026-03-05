# SEO Tracker

Last updated: 2026-03-05
Owner: GigWageTruth
Primary source: Google Search Console (GA4 pending access)

## Current Verdict
- Index/technical quality: improving
- Visibility (impressions/rank): improving fast
- Click conversion (CTR/clicks): weak and needs focused work
- Overall: good execution for this phase, but still early and unproven on traffic quality

## Segment Snapshot (2026-02-06 to 2026-03-05)
- `/best-cities/*`: 0 clicks / 382 impressions / avg pos 4.73
- `/compare/*`: 1 click / 387 impressions / avg pos 6.01
- `/salary/uber/*`: 1 click / 2,585 impressions / avg pos 7.44
- `/salary/doordash/*`: 0 clicks / 4,938 impressions / avg pos 5.87
- `/best-cities/doordash`: 0 clicks / 257 impressions / avg pos 4.84

Interpretation:
- Ranking is not the primary bottleneck anymore.
- Snippet-message fit and query intent fit are the main bottlenecks.

## Baseline Snapshot
- Current period: 2026-02-06 to 2026-03-05
- Previous period: 2026-01-09 to 2026-02-05
- Clicks: 3 -> 3 (flat)
- Impressions: 3,060 -> 7,084 (+131.5%)
- CTR: 0.098% -> 0.042% (-56.8%)
- Avg position: 10.25 -> 6.30 (improved)

## What Was Fixed (2026-03-05)
- Fixed JSON-LD escape issues causing Search Console inspection errors (`Bad escape sequence`).
- Moved key JSON-LD generation to backend Jackson serialization for safer escaping.
- Cleaned corrupted strings/tag artifacts in `city-report.jte`.
- Added/expanded regression tests to validate JSON-LD syntax on key SEO pages.

## Validation Result
- `generateJte`: pass
- `OrganicMonitoringRegressionTest`: pass
- `SitemapXmlTest`: pass

## Open Constraints
- GA4 property access is not connected yet, so behavior/conversion analysis is partial.
- Post-deploy recrawl lag means ranking/CTR impact is not measurable immediately.

## How To Judge If We Are Doing Well
- Rule 1: technical blockers must stay at 0 critical issues.
- Rule 2: impressions and average position should trend up week-over-week.
- Rule 3: CTR on pages ranking in positions 3-8 must rise over time.
- Rule 4: clicks must eventually follow impressions; if not, query intent/page match is off.

## CTR Benchmark Reference (Industry Heuristic)
- Position 1-3: often 10-30% CTR
- Position 4-6: often 4-10% CTR
- Position 7-10: often 1-4% CTR
- Our current site-level CTR (~0.04%) is far below these ranges, so snippet intent match remains the primary gap.

## 30-Day Targets
- Technical critical issues: 0
- Keep average position <= 7.0
- Raise site CTR from ~0.04% to >=0.10%
- Raise clicks from 3 per 28 days to >=8 per 28 days
- At least 3 landing pages with non-zero clicks in top impression group

## Expert Backlog (Priority)
- P0: Keep JSON-LD valid on all key templates (`best-cities`, `city-report`, `city-work-level`) and keep regression test green.
- P0: Rewrite title/meta templates for top-impression pages to match high-intent phrasing users actually click.
- P0: Tighten SERP snippet promise: include explicit value proposition (`after expenses`, `real hourly`, `updated`).
- P1: Re-balance indexing scope for low-yield pages if they keep generating impressions with persistent 0 clicks.
- P1: Improve on-page first screen for top entry pages to match title promise immediately.
- P1: Strengthen internal links from high-impression pages into calculator entry paths with intent-anchored anchor text.
- P2: After GA4 access, validate landing-page behavior (engagement, CTA clicks, calculator start rate) to separate SEO vs product friction.

## Title/Meta Rewrite Focus (First Batch)
- `https://gigverdict.com/best-cities/doordash`
- `https://gigverdict.com/best-cities/uber`
- `https://gigverdict.com/salary/doordash/*` template
- `https://gigverdict.com/salary/uber/*` template
- `https://gigverdict.com/compare/*` template

Definition of done for first batch:
- Updated templates deployed
- Re-index requested for top URLs
- 7-day and 28-day CTR deltas recorded in this file

## Shipped On 2026-03-05 (CTR Batch 1)
- Rewrote title/meta templates for:
- `/salary/{app}`
- `/best-cities/{app}`
- `/compare/{city}/uber-vs-doordash`
- `/salary/{app}/{city}`
- `/salary/{app}/{city}/{workLevel}`
- Added explicit snippet value props: `net after expenses`, per-city/per-work-level context, freshness signal (`Updated {month year}`), and estimated `$/hr` on city/work-level pages.

## Weekly Scorecard (fill every 7 days)
- Week ending:
- Clicks:
- Impressions:
- CTR:
- Avg position:
- Top 5 pages by impressions:
- Top 5 pages by clicks:
- Striking distance keywords (pos 8-15):
- Critical issues:
- Changes shipped this week:
- Next week focus:

## Immediate Next Actions
- Deploy current fixes.
- Re-check URL Inspection on `https://gigverdict.com/best-cities/doordash`.
- Re-check URL Inspection on `https://gigverdict.com/salary/doordash/denver/side-hustle`.
- Re-check URL Inspection on `https://gigverdict.com/salary/doordash/denver`.
- After 3-7 days, compare period again and update this file.
- Connect GA4 property access as final step.