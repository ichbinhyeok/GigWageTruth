# GigVerdict SEO Recovery Research - 2026-07-03

## Executive Thesis

GigVerdict did not mainly lose because of robots, canonical, sitemap, or a broad technical noindex issue. Live checks show key pages return 200, self-canonicalize correctly, and the sitemap exposes 262 URLs. The traffic pattern points to a search-intent identity regression after the 2026-04-24 "Tool-Identity Pivot Test": Google had been giving impressions for "average earnings / driver pay / by city / after expenses" informational queries, then the site reframed the same pages as "pay calculators." Around 2026-05-19, impressions dropped from roughly 916/day to 145/day.

The most likely recovery path is not publishing many more thin pages. It is to restore earnings-first SERP language, add credible field evidence from unstructured sources, and use the calculator as the differentiating module inside the page rather than as the search promise.

## Current Search Console Signal

Monthly trend:

| Month | Clicks | Impressions | Daily Impressions | Avg Position |
|---|---:|---:|---:|---:|
| 2026-02 | 2 | 4,727 | 169 | 7.20 |
| 2026-03 | 35 | 43,040 | 1,389 | 7.33 |
| 2026-04 | 12 | 33,885 | 1,130 | 8.15 |
| 2026-05 | 14 | 12,353 | 398 | 8.36 |
| 2026-06 | 26 | 4,008 | 134 | 11.00 |

Break point:

| Period | Impressions | Daily Impressions | Avg Position |
|---|---:|---:|---:|
| 2026-04-01 to 2026-05-18 | 43,988 | 916 | roughly 8 |
| 2026-05-19 to 2026-07-02 | 6,523 | 145 | roughly 10-11 |

## Why It Likely Happened

1. Search identity changed faster than authority could support.
   - Pre-change framing: "Average Uber Driver Earnings in {City}", "Highest-Paying Cities for DoorDash Drivers."
   - Current framing: "{App} {City} Pay Calculator", "Net Pay Calculator Ranking."
   - Google had already exposed query language around "average earnings", "driver earnings", "how much do drivers make", and "after expenses." The pivot moved the title/H1 away from those exact terms.

2. The calculator promise is weaker in SERP than the earnings answer.
   - Searchers asking "how much do Uber drivers make in Nashville" want a number first.
   - A calculator is useful after they trust the number. It should be the interactive proof, not the headline promise.

3. Information gain is too model-driven.
   - Current pages contain calculated values, but not enough field evidence: driver anecdotes, city-specific platform quirks, regulation/tipping changes, seasonal windows, acceptance-rate tradeoffs, and app-specific pay mechanics.
   - Winning pSEO pages usually combine template structure with hard-to-copy data: reviews, listings, real inventory, price feeds, salary reports, ratings, UGC, or transaction data.

4. The April `data-nosnippet` strategy may have reduced snippet material too aggressively.
   - Google says `data-nosnippet` controls what text can appear in snippets. Used surgically, it prevents bad snippets. Used broadly, it can leave Google fewer high-value passage candidates.

5. There is no evidence yet of a sitewide crawl block.
   - Live samples: `/best-cities/doordash` and `/salary/doordash/denver/side-hustle` return 200, self-canonicalize, and have no live `noindex`.
   - Sitemap is present at `https://gigverdict.com/sitemap.xml`.

## 30-Day Goal: 100 Monthly Clicks

The target is realistic only if recovery focuses on pages that already have some ranking memory. June produced 26 clicks. The goal is roughly 4x. Do not chase a giant page expansion first.

### Week 1 - Restore Intent and Snippets

1. Revert core title/H1 framing for the 20-30 highest-memory pages:
   - `/salary/{app}/{city}`
   - `/salary/{app}/{city}/{workLevel}` for indexable city/work-level pages
   - `/best-cities/{app}`
   - `/salary/{app}`

2. Title formula:
   - `Average {App} Driver Earnings in {City} ({Year}): ${Net}/hr After Expenses`
   - `{App} {City} Driver Pay After Gas and Taxes ({Year})`
   - `Highest-Paying Cities for {App} Drivers in {Year}`

3. H1 formula:
   - `Average {App} Driver Earnings in {City}`
   - `How much {App} drivers make in {City} after expenses`

4. Keep the calculator, but reposition it:
   - Above fold: answer-first earnings box.
   - Second module: "Adjust this estimate."
   - Schema: `FAQPage` and `BreadcrumbList` first; `WebApplication` only where the page is a true tool page.

5. Reduce `data-nosnippet` on sections that contain unique, answerable city evidence. Keep it only on ads, CTA clusters, and repeated nav blocks.

### Week 2 - Add Field Evidence Modules

Add a reusable "Driver field notes" section to priority city pages:

| Module | Purpose |
|---|---|
| Local driver reports | Summarize Reddit/forum anecdotes by city and app |
| Peak window notes | Lunch/dinner/weekend/airport/seasonality |
| Gross vs net warning | Explicit gas, mileage, tax, waiting-time distinction |
| Local regulation | Prop 22, NYC minimum pay/tip laws, local fee caps where relevant |
| Multi-app reality | Whether drivers report DoorDash, Uber Eats, or both as better |

### Week 3 - Publish 8 Evidence-Backed Pages

Do not publish 100 pages. Publish 8 upgraded pages that can win:

1. `/salary/uber/chicago`
2. `/salary/uber/los-angeles`
3. `/salary/uber/nashville`
4. `/salary/uber/portland`
5. `/salary/doordash/denver/side-hustle`
6. `/salary/doordash/atlanta`
7. `/salary/doordash/dallas`
8. `/best-cities/doordash`

Each should include 3-5 field notes and 2-3 external context sources.

### Week 4 - Internal Links and Recrawl

1. Add a "Popular driver earnings questions" hub:
   - "How much can I make in 2 hours?"
   - "Can I make $1,000/month?"
   - "Is DoorDash or Uber Eats better in {city}?"
   - "What is a good dollar-per-mile threshold?"

2. Link every upgraded page from:
   - homepage
   - salary directory
   - app hubs
   - best-cities pages
   - relevant blog posts

3. Submit sitemap and inspect top 10 changed URLs in Search Console.

## 50 Unstructured / Field-Evidence Sources

These are not all "truth." Treat them as field signals to synthesize, label, and contrast against model estimates.

| # | Source | What To Mine |
|---:|---|---|
| 1 | [Reddit: "What's a decent hourly rate?" DoorDash drivers](https://www.reddit.com/r/doordash_drivers/comments/1rw10d9/whats_a_decent_hourly_rate/) | Driver language around acceptable hourly pay |
| 2 | [Reddit: Chicago Uber earnings](https://www.reddit.com/r/uberdrivers/comments/1svtznm/chicago_uber_earnings/) | Chicago daily gross expectations and tip dependency |
| 3 | [Reddit: Uber Eats hourly earnings](https://www.reddit.com/r/UberEATS/comments/1jmumth/how_much_do_you_earn_hour/) | Active vs online hour expectations |
| 4 | [Reddit: DoorDash/Grubhub 8-10 hour earnings](https://www.reddit.com/r/couriersofreddit/comments/1dp86kf/how_much_earnings_can_i_expect_doing/) | Multi-app earnings expectations |
| 5 | [Reddit: DoorDash 2026 so far](https://www.reddit.com/r/doordash_drivers/comments/1qb9hru/2026_so_far/) | 2025 vs 2026 perceived pay changes |
| 6 | [Reddit: Uber $1,000/month in summer 2026](https://www.reddit.com/r/uberdrivers/comments/1tc9lv0/realistically_how_much_driving_do_i_need_to_do_to/) | Monthly income planning query |
| 7 | [Reddit: Uber Eats after-expense hourly math](https://www.reddit.com/r/UberEATS/comments/1s4r0pq/heres_how_much_i_earn_per_hour/) | Online hour vs active hour net framing |
| 8 | [Reddit: Reasonable daily delivery earnings](https://www.reddit.com/r/couriersofreddit/comments/1m2ag2c/how_much_a_day_can_i_reasonably_make_with/) | Day-level gross targets and bad-order examples |
| 9 | [Reddit: DoorDash $1.45/hour analysis discussion](https://www.reddit.com/r/doordash_drivers/comments/1iqy7jl/doordash_drivers_make_an_average_of_145_an_hour/) | Expense/tax pushback and driver counter-math |
| 10 | [Reddit: MIT Uber/Lyft minimum wage discussion](https://www.reddit.com/r/uberdrivers/comments/1tn2epr/mit_study_finds_74_of_uberlyft_drivers_earn_less/) | Driver reactions to low net-pay studies |
| 11 | [Reddit: Is Uber Eats worth it in 2025?](https://www.reddit.com/r/UberEATS/comments/1m2gfct/is_it_worth_being_an_ubereats_driver_in_2025/) | Side-hustle decision framing |
| 12 | [Reddit: $100k courier year](https://www.reddit.com/r/couriersofreddit/comments/t38p0z/100k_per_year/) | Extreme high-earner pattern and workload |
| 13 | [Reddit: Part-time DoorDash weekly earnings](https://www.reddit.com/r/doordash_drivers/comments/1hfrk97/how_much_do_you_make_weekly_as_a_parttime/) | Part-time weekly ranges |
| 14 | [Reddit: Uber algorithm and $20-25/hour claim](https://www.reddit.com/r/uberdrivers/comments/1tziq4e/ubers_algorithm_is_designed_to_keep_drivers/) | Driver perception of earnings caps |
| 15 | [Reddit: Courier work advice](https://www.reddit.com/r/UberEATS/comments/1py1vii/general_questions_about_courier_work/) | $/mile thresholds and wait-time traps |
| 16 | [Reddit: Tracking real profit after gas](https://www.reddit.com/r/couriersofreddit/comments/1rp6age/how_do_you_guys_track_your_real_profit_after_gas/) | Pain point for real-profit tracking |
| 17 | [Reddit: $28/hour over 78 hours](https://www.reddit.com/r/doordash_drivers/comments/1k4ptr8/about_28_an_hour_over_78_hours_not_bad_at_all/) | Prop 22 and active-time math |
| 18 | [Reddit: How much lower driver pay will go in 2026](https://www.reddit.com/r/uberdrivers/comments/1q2jdnr/how_much_lower_driver_pay_will_go_in_2026/) | Fear of pay compression |
| 19 | [Reddit: NYC Uber Eats minimum pay and tips](https://www.reddit.com/r/UberEATS/comments/18lkb0q/nyc_uber_delivery_new_minimum_wage_and_asking_for/) | Minimum-pay/tipping confusion |
| 20 | [Reddit: Uber Eats pay slashed discussion](https://www.reddit.com/r/couriersofreddit/comments/15tqvxt/uber_eats_has_slashed_its_already_meager_pay_from/) | Base-pay dissatisfaction |
| 21 | [Reddit: DoorDash hourly earnings](https://www.reddit.com/r/doordash_drivers/comments/1i3pdi9/how_much_do_you_make_hourly/) | Typical claimed hourly range |
| 22 | [Reddit: 2026 Uber driver outlook](https://www.reddit.com/r/uberdrivers/comments/1pzhg9k/2026_you_will_make_more/) | Union/minimum-pay sentiment |
| 23 | [Reddit: Australian gig minimum wage discussion](https://www.reddit.com/r/UberEATS/comments/1ppjojt/how_is_this_legal/) | International minimum active-hour framing |
| 24 | [Reddit: Multi-app tax filing question](https://www.reddit.com/r/couriersofreddit/comments/vpvt1j/if_i_delivery_for_both_ubereats_and_doordash_do_i/) | Tax confusion for small earners |
| 25 | [Reddit: Is it worth coming back to dashing in 2026?](https://www.reddit.com/r/doordash_drivers/comments/1rs4kmu/is_it_worth_coming_back_to_dashing_in_2026/) | Re-entry decision and 8-hour earning claims |
| 26 | [Reddit: Uber driver in 2026 worth it?](https://www.reddit.com/r/UberEatsDrivers/comments/1q391xs/is_it_worth_being_an_uber_driver_in_2026/) | Market-dependent upside claims |
| 27 | [Reddit: DoorDash pay in Dallas](https://www.reddit.com/r/doordash_drivers/comments/17waa1d/doordasher_pay_in_dallas/) | Dallas beginner pay confusion |
| 28 | [Reddit: DoorDash pay in Colorado](https://www.reddit.com/r/doordash_drivers/comments/18q780q/whats_the_pay_like_in_colorado/) | Colorado/Denver market questions |
| 29 | [Reddit: Phoenix/Scottsdale slowdown anecdote](https://www.reddit.com/r/doordash_drivers/comments/1q35vr8/im_here_to_admit_that_everybody_else_was_right/) | Phoenix order frequency and $/mile complaints |
| 30 | [Reddit: Uber daily targets reality check](https://www.reddit.com/r/uberdrivers/comments/1rbmmrn/is_250400_a_day_realistic_need_reality_check_from/) | LA daily gross realism |
| 31 | [Reddit: DoorDash Dallas area](https://www.reddit.com/r/doordash_drivers/comments/1lz81gl/how_is_doordash_in_dallas_area/) | Dallas zone/suburb questions |
| 32 | [Reddit: Uber LA average pay](https://www.reddit.com/r/uberdrivers/comments/1jbhbgu/average_pay_in_los_angeles/) | LA market transfer expectations |
| 33 | [Reddit: DoorDash take-home pay accepting every order](https://www.reddit.com/r/doordash_drivers/comments/1mfpmnd/doordash_driver_reveals_his_takehome_pay_after/) | Accept-all strategy and take-home skepticism |
| 34 | [Reddit: Phoenix dead for DoorDash](https://www.reddit.com/r/doordash_drivers/comments/1ljx4lm/super_dead_in_phx_az_2hrs_for_one_good_order/) | Phoenix day/time notes |
| 35 | [Reddit: Texas full-time DoorDash pay](https://www.reddit.com/r/doordash_drivers/comments/ppo8p9/anyone_dash_in_texas_fulltime_how_is_the_pay/) | Texas market-level Q&A |
| 36 | [Reddit: LA Uber below minimum wage after expenses](https://www.reddit.com/r/uberdrivers/comments/11fkdcf/less_then_minimum_wage_now_in_los_angeles_area_now/) | Gross vs net wage framing |
| 37 | [Reddit: Full-time Dasher week in Colorado](https://www.reddit.com/r/doordash_drivers/comments/1s9qg0l/could_anyone_show_a_realistic_week_as_a_full_time/) | Full-time proof request |
| 38 | [Reddit: West Phoenix early morning note](https://www.reddit.com/r/doordash_drivers/comments/1s4qaqi/im_stressed/) | Time-window advice by area |
| 39 | [Reddit: Dallas suburb busy times](https://www.reddit.com/r/doordash_drivers/comments/13kj8gb/how_do_you_guys_make_so_much_money_each_week/) | Dallas lunch/dinner timing |
| 40 | [Reddit: DoorDash hourly pay calculator spreadsheet](https://www.reddit.com/r/doordash_drivers/comments/1lxsrcy/doordash_hourly_pay_calculator/) | User-built calculator pain point |
| 41 | [Gridwise 2026 Annual Gig Mobility Report](https://gridwise.io/analytics/2026-annual-gig-mobility-report) | Platform economics and driver pay trends |
| 42 | [Business Insider: 12 gig workers' annual earnings](https://www.businessinsider.com/how-much-gig-workers-make-uber-doordash-taskrabit-earnings-2026-4) | First-person annual income ranges |
| 43 | [Business Insider: Gig workers quitting apps](https://www.businessinsider.com/gig-workers-quitting-uber-doordash-to-find-full-time-jobs-2026-2) | Churn and reduced bonus context |
| 44 | [NYC DCWP: $550M tip loss report](https://www.nyc.gov/site/dca/news/005-26/dcwp-report-shows-uber-doordash-drove-550-million-delivery-worker-pay-losses) | NYC tip-interface/pay-law data |
| 45 | [IRS 2026 mileage rate](https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents) | 72.5 cents/mile expense assumption |
| 46 | [DoorDash official Dasher pay](https://dasher.doordash.com/en-us/about/pay) | Earn by Time vs offer mechanics |
| 47 | [Uber official driver earnings](https://www.uber.com/us/en/drive/how-much-drivers-make/) | Uber pay calculation caveats |
| 48 | [DoorDash Prop 22 guide](https://help.doordash.com/en-us/dashers/article/california-dashers) | Active-time/mileage guarantee mechanics |
| 49 | [Uber active-hour pilot explanation](https://www.uber.com/us/en/blog/piloting-estimated-earnings-per-active-hour-on-trip-requests/) | Active-hour offer math |
| 50 | [NerdWallet DoorDash field test](https://www.nerdwallet.com/finance/learn/how-much-does-doordash-pay) | First-person field test with expense math |

## 50 pSEO Examples To Study

| # | Example | Pattern To Study |
|---:|---|---|
| 1 | [Zapier integrations](https://zapier.com/apps) | App + app integration pages |
| 2 | [Make app integrations](https://www.make.com/en/integrations) | Zapier-like workflow pages |
| 3 | [IFTTT applets](https://ifttt.com/explore) | Trigger/action long-tail pages |
| 4 | [Wise currency converter](https://wise.com/us/currency-converter/) | Converter pages with live rates |
| 5 | [Wise SWIFT/BIC pages](https://wise.com/us/swift-codes/) | Bank-code lookup pages |
| 6 | [Canva templates](https://www.canva.com/templates/) | Template gallery as product pages |
| 7 | [Webflow templates](https://webflow.com/templates) | Template + category pages |
| 8 | [VEED tools](https://www.veed.io/tools) | Utility/tool landing pages |
| 9 | [Kapwing tools/templates](https://www.kapwing.com/tools) | Creator task pages |
| 10 | [Airtable templates](https://www.airtable.com/templates) | Template library taxonomy |
| 11 | [G2 comparisons](https://www.g2.com/compare) | Reviews-backed software comparisons |
| 12 | [Capterra directories](https://www.capterra.com/) | Software category/review pages |
| 13 | [Product Hunt alternatives](https://www.producthunt.com/) | Product/alternative discovery |
| 14 | [AlternativeTo](https://alternativeto.net/) | Alternative pages by product |
| 15 | [Retool templates](https://retool.com/templates) | Internal-tool templates |
| 16 | [Pipedrive comparisons](https://www.pipedrive.com/en/compare) | Competitor comparison pages |
| 17 | [HubSpot templates](https://www.hubspot.com/resources) | Template/resource taxonomies |
| 18 | [Notion templates](https://www.notion.com/templates) | Community/product templates |
| 19 | [Shopify App Store](https://apps.shopify.com/) | Marketplace app category/listing pages |
| 20 | [Atlassian Marketplace](https://marketplace.atlassian.com/) | App marketplace SEO |
| 21 | [Salesforce AppExchange](https://appexchange.salesforce.com/) | Enterprise marketplace pages |
| 22 | [Calendly integrations](https://calendly.com/integrations) | App/tool integration pages |
| 23 | [DelightChat Shopify app pages](https://www.delightchat.io/best-shopify-apps) | "Best X for Y" dataset pages |
| 24 | [MentorCruise mentors](https://mentorcruise.com/mentor/browse/) | Category + expert inventory pages |
| 25 | [Tripadvisor things to do](https://www.tripadvisor.com/Attractions) | Destination + activity pages |
| 26 | [Booking.com destination pages](https://www.booking.com/) | Hotels in city pages |
| 27 | [Airbnb locations/categories](https://www.airbnb.com/) | Location + stay inventory |
| 28 | [Expedia destination pages](https://www.expedia.com/) | Travel inventory pages |
| 29 | [Yelp city/category pages](https://www.yelp.com/) | Local category + city pages |
| 30 | [Eater city maps](https://www.eater.com/maps) | Editorialized city/category lists |
| 31 | [The Infatuation city guides](https://www.theinfatuation.com/) | Restaurant city/category guides |
| 32 | [Zillow homes by city](https://www.zillow.com/homes/) | Real estate inventory pages |
| 33 | [Redfin city pages](https://www.redfin.com/) | Real estate city/listing pages |
| 34 | [Realtor.com city listings](https://www.realtor.com/) | Location + property inventory |
| 35 | [Apartments.com city pages](https://www.apartments.com/) | Rental inventory pages |
| 36 | [Zumper rent reports](https://www.zumper.com/rent-research) | Rent data by city |
| 37 | [RentCafe market trends](https://www.rentcafe.com/average-rent-market-trends/us/) | Rent trend pages |
| 38 | [moveBuddha routes/city pages](https://www.movebuddha.com/) | Moving route + city cost pages |
| 39 | [Nomad List city pages](https://nomads.com/) | City data + community notes |
| 40 | [Numbeo cost of living](https://www.numbeo.com/cost-of-living/) | City cost comparison data |
| 41 | [Payscale salary pages](https://www.payscale.com/research/US/Country=United_States/Salary) | Salary by job/skill/employer |
| 42 | [Salary.com salary pages](https://www.salary.com/research/salary) | Salary by job/location |
| 43 | [Indeed jobs](https://www.indeed.com/) | Job title + location pages |
| 44 | [LinkedIn jobs](https://www.linkedin.com/jobs/) | Job inventory pages |
| 45 | [Glassdoor salaries](https://www.glassdoor.com/Salaries/index.htm) | Salary/company review pages |
| 46 | [ZipRecruiter salaries](https://www.ziprecruiter.com/Salaries) | Salary by role/location |
| 47 | [Built In salaries](https://builtin.com/salaries) | Tech salary/location pages |
| 48 | [in2013dollars inflation pages](https://www.in2013dollars.com/) | Historical inflation calculators |
| 49 | [ColorHexa color pages](https://www.colorhexa.com/) | Data-rich lookup pages |
| 50 | [World Population Review](https://worldpopulationreview.com/) | State/city/stat pages |

## Winner Patterns

| Pattern | Winners Do | GigVerdict Now |
|---|---|---|
| Search promise | Match the exact query language | Shifted toward internal product language: "calculator" |
| Data moat | Use real inventory, reviews, UGC, transaction data, or official feeds | Mostly modeled estimates and static city assumptions |
| Page uniqueness | Each URL has unique, query-satisfying information | City pages differ numerically but need more field evidence |
| User outcome | Answer first, then tool/action | Tool-first framing before trust is established |
| Snippet control | Provide a clear extractable answer block | Some useful sections are hidden from snippets |
| Quality gates | Noindex/merge weak inventory combinations | Has gates, but needs evidence threshold, not only city tier/rich local data |
| Internal linking | Hubs explain category logic and push users deeper | Good structure, but anchors should return to earnings language |
| Freshness | Updated data and source dates visible | Has updated dates, but not enough source/date provenance per city |
| Authority | Pages cite primary/field data | Needs visible source trail and field-note methodology |
| Expansion | Scale after a template proves | Needs recovery of top pages before more page count |

## Actionable Repositioning

### New Page Architecture

1. SERP title: earnings query.
2. H1: same earnings query.
3. First paragraph: direct answer with net hourly estimate.
4. Evidence row: updated date, IRS mileage rate, gas assumption, active vs online time caveat.
5. Field notes: 3-5 summarized driver anecdotes or third-party data points.
6. Calculator module: adjust gross, miles, hours, gas.
7. Work-level table.
8. FAQ: exact questions from Search Console and Reddit.
9. Internal links: compare page, best-cities, app hub, methodology.

### 100 Click Forecast

If impressions recover from 4,000/month to 12,000/month and CTR rises to 0.8%, that is 96 clicks/month. This is the cleanest near-term path. Trying to get 100 clicks from the current 4,000 monthly impressions would require 2.5% CTR, which is unrealistic for generic gig-pay SERPs without rich snippets or strong brand trust.

### Near-Term Priority

1. Restore earnings-first metadata.
2. Add field evidence to 8 pages.
3. Reduce over-broad `data-nosnippet`.
4. Resubmit recrawl.
5. Monitor impressions first, CTR second.

## Sources Used

- Google traffic drop debugging: https://developers.google.com/search/docs/monitor-debug/debugging-search-traffic-drops
- Google spam policies: https://developers.google.com/search/docs/essentials/spam-policies
- Google robots meta and data-nosnippet: https://developers.google.com/search/docs/crawling-indexing/robots-meta-tag
- Practical Programmatic examples: https://practicalprogrammatic.com/examples
- Directive pSEO examples/patterns: https://directiveconsulting.com/blog/blog-programmatic-seo-examples/
- BCMS pSEO examples: https://thebcms.com/blog/programmatic-seo-examples
- Zapier pSEO guide: https://zapier.com/blog/programmatic-seo/
- Indexed pSEO examples: https://www.joinindexed.com/blog/programmatic-seo-examples
- Search Engine Land pSEO examples: https://searchengineland.com/programmatic-seo-real-world-examples-insights-433490
- Gridwise 2026 Gig Mobility Report: https://gridwise.io/analytics/2026-annual-gig-mobility-report
- Gridwise DoorDash earnings: https://gridwise.io/blog/how-much-do-doordash-drivers-make
- Gridwise Uber earnings: https://gridwise.io/blog/how-much-do-uber-drivers-make
- IRS 2026 mileage rate: https://www.irs.gov/newsroom/irs-sets-2026-business-standard-mileage-rate-at-725-cents-per-mile-up-25-cents
- NYC DCWP tipping/pay report: https://www.nyc.gov/site/dca/news/005-26/dcwp-report-shows-uber-doordash-drove-550-million-delivery-worker-pay-losses
- DoorDash pay: https://dasher.doordash.com/en-us/about/pay
- Uber pay: https://www.uber.com/us/en/drive/how-much-drivers-make/
