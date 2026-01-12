# PROMPT_CONTEXT.md - GigWageTruth

## 1. Project Overview
**GigWageTruth** is a US-market niche SaaS designed to calculate the *True Net Hourly Wage* of gig workers (Uber, Lyft, etc.).
It uses a "Brutal Verdict" concept to shock users by showing their real earnings after hidden costs.

## 2. Technical Manifesto (STRICT)
- **Stack**: Spring Boot 3, Java 17, **jte (Java Template Engine)**.
- **Styling**: **Tailwind CSS** (CDN integration only). No custom CSS files.
- **Architecture**:
  - `src/main/jte/layout/main.jte` is the MASTER layout.
  - All numbers MUST come from `com.gigwager.util.AppConstants`.
  - Logic MUST be in `@Service` (Java) or explicit Alpine.js (Client) for interactivity.
  
## 3. Key Formulas (AppConstants.java)
- `IRS_MILEAGE_RATE` = $0.67/mile
- `TAX_RATE` = 15.3% (Self Employment)
- Formula: `Gross - (Miles/MPG * Gas) - (Miles * 0.67) - (Gross * 15.3%)`

## 4. Current State (Step 5 Completed)
- Project initialized (Gradle, Spring Boot).
- `CalculatorService` implemented (Java Logic).
- `uber.jte` implemented (UI + Alpine.js Logic mirroring Java).
- `main.jte` implemented (Layout + Tailwind + SEO placeholders).
- `SeoMeta` record created.
- JSON-LD implemented in `uber.jte`.

## 5. Next Steps
- Add more pages (DoorDash, Lyft specific calculators).
- Implement backend logic to save results/share (Viral features).
- Deploy to Railway.
