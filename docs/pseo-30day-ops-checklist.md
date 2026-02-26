# GigWageTruth pSEO 30-Day Continuity Checklist

Last updated: 2026-02-26  
Owner intent: maintain pSEO quality while scaling from 20 city rich-content files to 40+ without thin-content regression.

## 1. Current Snapshot

- Rich city JSON files are loaded from `src/main/resources/data/cities/*.json`.
- Current count target: 20 files.
- Controller injects rich content into work-level pages when file exists:
  - `src/main/java/com/gigwager/controller/ProgrammaticSeoController.java`
- Validation is fail-fast at startup:
  - `src/main/java/com/gigwager/service/CityRichContentRepository.java`
- Layout variance is active in template:
  - `src/main/jte/salary/city-work-level.jte`
- Core tests:
  - `src/test/java/com/gigwager/CityRichContentRepositoryTest.java`
  - `src/test/java/com/gigwager/CityWorkLevelLayoutTypeTest.java`
  - `src/test/java/com/gigwager/CityRichContentDuplicationGuardTest.java`

## 2. First 15-Minute Return Checklist (After 30 Days)

1. Pull latest branch and check dirty files.
2. Run full test suite:
   - `.\gradlew.bat test --no-daemon`
3. Verify rich-content file count:
   - `Get-ChildItem src/main/resources/data/cities/*.json | Measure-Object`
4. Spot-check three pages in browser:
   - `/salary/uber/new-york/part-time`
   - `/salary/uber/los-angeles/side-hustle`
   - `/salary/uber/washington-dc/full-time`
5. Confirm page contains:
   - `Persona Validation Snapshot`
   - `Sources and Methodology`
   - expected `TYPE_A/B/C` section order behavior

## 3. SEO/AEO/SERP Health Checks

## GSC (Google Search Console)

1. Indexing report:
   - Track valid indexed count for `/salary/*/*/*`.
   - Alert if "Crawled - currently not indexed" grows for rich pages.
2. Search performance:
   - Compare last 28 days vs previous 28 days.
   - Segment by query containing city + app + work level.
3. URL inspection:
   - Inspect at least 5 rich pages and confirm rendered content includes persona + sources blocks.

## SERP Quality

1. Manual query checks for 5 priority markets:
   - `uber side hustle pay [city]`
   - `doordash full time pay [city]`
2. Observe:
   - snippet relevance to local strategy text
   - cannibalization between city page and work-level page

## AEO Readiness

1. Confirm FAQ JSON-LD still valid on work-level pages.
2. Check if answer snippets extract:
   - net-hourly range
   - methodology wording
3. Keep source timestamps fresh (`checkedAt`) for top traffic cities.

## 4. Data Quality Gates Before Adding New Cities

All new city JSON files must pass:

1. `citySlug` == filename slug.
2. `seo.heroHook` length >= 60.
3. `workLevels` includes all:
   - `part-time`, `side-hustle`, `full-time`
4. Each work level:
   - `localStrategyText` length >= 80
   - HTML blocks length >= 80
   - `personaQuotes` >= 2
   - at least 2 unique `personaType` values
5. Sources:
   - at least 2 per city (currently using 5)
   - URLs start with `http://` or `https://`
   - `checkedAt` ISO date

## 5. 30-Day Execution Plan (What To Do Next)

1. Expand from 20 to 30 cities:
   - prioritize markets with existing search impressions first.
2. Add anti-duplication lint step:
   - CI test: `CityRichContentDuplicationGuardTest`
   - computes 3-gram similarity across heroHook + localStrategy + painPoints + personaQuotes
   - fails if pairwise score exceeds threshold constant (`MAX_PAIRWISE_SIMILARITY`)
3. Add freshness job:
   - monthly update `lastVerifiedAt` and critical source `checkedAt`.
4. Add measurement dashboard:
   - indexed URLs
   - clicks/impressions/CTR by city
   - average position by work level
5. After stability, scale 30 -> 40+.

## 6. Regression Signals (Stop and Fix)

Stop rollout if any occur:

1. Index coverage drops for rich pages over two consecutive weeks.
2. CTR falls >20% on top 10 rich URLs without ranking gain.
3. Spike in duplicate-like snippets across different city pages.
4. Startup failure due repository validation errors.

## 7. Quick Commands

- run tests:
  - `.\gradlew.bat test --no-daemon`
- run only duplication guard:
  - `.\gradlew.bat test --no-daemon --tests com.gigwager.CityRichContentDuplicationGuardTest`
- list rich files:
  - `Get-ChildItem src/main/resources/data/cities/*.json`
- find page-structure usage:
  - `rg \"pageStructureType\" src/main/resources/data/cities`
- validate controller wiring:
  - `rg \"cityRichContentRepository|personaQuotes|sourceCitations\" src/main/java/com/gigwager/controller/ProgrammaticSeoController.java`

## 8. Notes for Next Operator

- Do not weaken validation rules to "make files load."
- Prefer fewer high-quality city files over mass-generated near-duplicates.
- Keep persona attribution transparent; do not imply unverifiable claims.
- Preserve method/version metadata to support trust and auditability.
