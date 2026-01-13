# Role
Act as a Senior Full-Stack Engineer and SEO Specialist with expertise in Google AdSense approval strategies.

# Project Context
I am building **GigWageTruth**, a niche calculator website for Uber and DoorDash drivers to calculate their *true* net hourly wage after expenses (gas, depreciation, taxes).
- **Tech Stack**: Java Spring Boot, JTE (Server-Side Templating), Alpine.js (Client-side logic), Tailwind CSS.
- **Goal**: Achieve Google AdSense approval and high organic rankings for keywords like "Uber Driver Pay Calculator" and "Real DoorDash Wages".

# Verification Task
Please review my core implementation files for **SEO effectiveness**, **AdSense readiness**, and **Code Quality**.

## 1. Files to Analyze

**A. Core Templates (Calculators)**
- `src/main/jte/uber.jte`: The main Uber calculator page.
- `src/main/jte/doordash.jte`: The DoorDash calculator page.
*Look for: Semantic HTML, depth of "SSR" textual content (Hero/Analysis/FAQ), and correct implementation of `WebApplication` and `FAQPage` JSON-LD schemas.*

**B. Controller Logic**
- `PageController.java`: Handles routing for blog and static pages.
- `SitemapController.java`: Generates the dynamic XML sitemap.
*Look for: Proper route handling, correct Sitemap XML structure, and headers.*

**C. Content Strategy (Blog)**
- `src/main/jte/blog/index.jte`: The blog hub.
- `src/main/jte/blog/multi-apping-guide.jte`: A sample seed article.
*Look for: Internal linking structure, content quality signals (E-E-A-T), and user engagement elements.*

## 2. Specific Questions to Answer

1.  **AdSense Eligibility**: Does the site structure (Calculator + Blog + Trust Pages) look sufficient to pass the "Low Value Content" check? Is the text content on the calculator pages unique enough?
2.  **Technical SEO**: I have injected `application/ld+json` (FAQPage, WebApplication) directly into the body body of the JTE templates. Is this valid? Will Google parse it correctly?
3.  **Thin Content Risk**: The `uber.jte` and `doordash.jte` share a similar layout. Have I differentiated the textual content (Introduction, Verdict, FAQs) enough to avoid a "Duplicate Content" penalty?
4.  **Improvements**: What is the ONE most critical missing feature that would guarantee AdSense approval?

# Constraints
- Focus on High-Level Strategy and SEO technicalities.
- Do not nitpick minor CSS preferences unless they hurt UX/CLS (Cumulative Layout Shift).
