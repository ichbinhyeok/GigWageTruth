# PR8 / P0 — Pre-Deploy Trust & SEO Audit Report

**Date**: 2026-02-24
**Author**: Antigravity (AI-assisted)
**Scope**: YMYL compliance, copy-model alignment, AEO enhancements

---

## C) WorkLevel.java — YMYL/Trust Rewrite

### Changes Summary

| Location | Before (Overclaim) | After (Hedged) |
|----------|-------------------|----------------|
| `getTaxStrategy()` FULL_TIME | "**Mandatory Quarterly Payments:** You'll owe $3,000-5,000 quarterly in taxes" | "**Quarterly Estimated Taxes:** Depending on your earnings, you may owe several thousand dollars per quarter" |
| `getTaxStrategy()` FULL_TIME | "**Retirement Tax Hack:** Contribute up to $23,000 as 'employee' + 25% as 'employer'" | "**Retirement Tax Strategy:** Check IRS.gov for current limits. Consult a CPA for optimal setup" |
| `getTaxStrategy()` FULL_TIME | "S-Corp Election: netting $60K+/year" | "S-Corp Election: netting significant income... optimal threshold varies by state" |
| `getTaxStrategy()` PART_TIME | "No Quarterly Taxes Needed: the IRS won't penalize you" | "Quarterly Taxes May Not Apply: penalty is often minimal. Check your total liability" |
| `getTaxStrategy()` SIDE_HUSTLE | "Audit Risk: Low. IRS flags... not side-hustlers making $25K/year" | "Audit Risk: Generally low... always keep clean records" |
| `getWorkLevelMeaning()` FULL_TIME | "expect to pay $300-600... married with kids $800-1,200" | "premiums can range from roughly $300-600 depending on age and household size (check Healthcare.gov)" |
| `getWorkLevelMeaning()` FULL_TIME | "contribute up to $66,000/year (2024 limit)" | "generous contribution limits (check IRS.gov for current year limits)" |
| `getBestPractices()` PART_TIME | "Maintain a 70%+ acceptance rate to avoid deactivation" | "acceptance rate thresholds vary by platform and region — check your app's current requirements" |
| `getBestPractices()` SIDE_HUSTLE | "that's a $9,425 tax deduction you CAN'T afford to lose" | "your mileage deduction could be substantial (multiply miles × current IRS rate)" |
| All `getTaxStrategy()` | No disclaimer | Added "Tax information is for educational purposes only — not tax advice. Consult a qualified CPA. Last reviewed: 2026-02." |

### Rationale
- Removed hardcoded dollar amounts for tax obligations (change year-to-year, state-to-state)
- Removed stale 2024 contribution limits (these are updated annually by the IRS)
- Converted platform-specific thresholds ("70%+ acceptance rate") to variable language since policies change
- Added "Last reviewed" timestamp for YMYL transparency

---

## D) Copy-Model Alignment (Calculation Methodology)

### Problem
Our Standard Mode calculation uses the **IRS flat rate of $0.725/mile**, NOT individual city gas prices. However, several copy elements implied that we "deduct gas at local prices" — this is misleading per YMYL standards.

### Changes

| File | Before | After |
|------|--------|-------|
| `city-report.jte` hero (L62-63) | "after {city}'s gas prices ($X/gal), vehicle costs, and taxes" | "after mileage costs (IRS rate: $0.725/mi), vehicle depreciation, and taxes. Local gas: $X/gal." |
| `ProgrammaticSeoController.java` highTraffic meta | "Estimate your liquid hourly wage after gas ($X/gal) and depreciation" | "Estimate your net hourly wage using the IRS mileage rate ($0.725/mi) plus taxes. Local gas: $X/gal." |
| `ProgrammaticSeoController.java` cheapGas meta | "Use our Net Pay Calculator to estimate your real take-home pay" | "Use our Net Pay Calculator (IRS mileage proxy + SE tax) to see your real take-home." |
| `ProgrammaticSeoController.java` highCost meta | "We estimate the exact breakdown of Expenses vs. Profit" | "We estimate net profit using the IRS standard mileage rate and SE tax" |
| `ProgrammaticSeoController.java` default meta | "We deduct estimated gas ($X/gal), taxes, and wear & tear" | "We use the IRS mileage rate ($0.725/mi) and 15.3% SE tax" |

### Rationale
- **Accuracy**: The IRS Standard Mileage Rate is a *proxy* that bundles gas, depreciation, insurance, and maintenance into one flat rate. Saying "we deduct gas" implies we use actual gas prices, which we don't in Standard mode.
- **Gas price is still shown**: Repositioned as "Local gas: $X/gal" — a reference point for cashflow awareness, not a calculation input.
- **Advanced Mode exception**: In "My Car (Precise)" mode, gas price IS used directly. This copy only applies to the default SSR pages which use Standard mode.

---

## Additional Changes in This PR

### UX: Vehicle Preset Expansion (vehicle-presets.json)
Added 9 popular vehicles:
- **Gas sedans**: Toyota Camry, Nissan Altima, Chevy Malibu, Honda Accord
- **Gas SUVs**: Toyota Highlander, RAV4 Gas, Mazda CX-5, Ford Explorer
- **Minivan**: Toyota Sienna Hybrid

### AEO: AI Search Optimization (city-report.jte)
1. **TL;DR Summary Table**: Semantic `<table>` positioned at page top with `aria-label`, `<caption>`, `<thead>`, `<tfoot>` for maximum AI crawlability
2. **Expanded FAQ JSON-LD**: 3 → 7 questions targeting long-tail AI searches:
   - "How much does a {App} driver make in {City} after expenses in 2026?"
   - "Is {App} worth it in {City} in 2026?"
   - "What is the best car for {App} driving in {City}?"
   - "How much should a {App} driver in {City} save for taxes?"
   - "Is it better to drive for Uber or DoorDash in {City}?"

---

## Risk Reduced

| Risk | Status |
|------|--------|
| YMYL penalty from definitive tax claims | ✅ Mitigated |
| Stale IRS limits (2024 data in 2026 site) | ✅ Removed |
| Copy-model misalignment (gas vs IRS proxy) | ✅ Fixed |
| Low vehicle preset coverage (hybrid-only bias) | ✅ Expanded |
| Weak AEO/AI search presence | ✅ Enhanced |
