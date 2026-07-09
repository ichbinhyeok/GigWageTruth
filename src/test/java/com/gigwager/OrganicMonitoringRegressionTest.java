package com.gigwager;

import com.gigwager.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrganicMonitoringRegressionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void topLandingPagesShouldPassSeoBaselinesAndGenerateReport() throws Exception {
        MvcResult sitemapResult = mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andReturn();
        String sitemap = sitemapResult.getResponse().getContentAsString();

        List<String> topUrls = extractPageUrlsFromSitemap(sitemap).stream()
                .filter(url -> url.startsWith(AppConstants.BASE_URL + "/"))
                .limit(20)
                .toList();

        List<String> failures = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<Map<String, String>> rows = new ArrayList<>();

        for (String absoluteUrl : topUrls) {
            String path = absoluteUrl.replace(AppConstants.BASE_URL, "");
            if (path.isBlank()) {
                path = "/";
            }

            MvcResult pageResult = mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andReturn();
            String html = pageResult.getResponse().getContentAsString();
            Document doc = Jsoup.parse(html, absoluteUrl);

            String title = doc.title() != null ? doc.title().trim() : "";
            String description = metaContent(doc, "description");
            String canonical = canonicalHref(doc);
            String robots = metaContent(doc, "robots");
            String h1 = firstH1(doc);
            boolean hasFaqJsonLd = html.contains("\"@type\": \"FAQPage\"") || html.contains("\"@type\":\"FAQPage\"");
            boolean hasVisibleFaq = doc.text().contains("Frequently Asked Questions");

            if (title.isBlank()) {
                failures.add(path + " missing <title>");
            } else if (title.length() > 80) {
                failures.add(path + " title too long (" + title.length() + ")");
            } else if (title.length() > 68) {
                warnings.add(path + " title may truncate (" + title.length() + ")");
            }

            if (description.isBlank()) {
                failures.add(path + " missing meta description");
            } else if (description.length() < 70) {
                warnings.add(path + " short meta description (" + description.length() + ")");
            } else if (description.length() > 185) {
                warnings.add(path + " long meta description (" + description.length() + ")");
            }

            boolean canonicalMatchesDomain = canonical.equals(AppConstants.BASE_URL)
                    || canonical.startsWith(AppConstants.BASE_URL + "/");
            if (canonical.isBlank() || !canonicalMatchesDomain) {
                failures.add(path + " invalid canonical: " + canonical);
            }

            if (h1.isBlank()) {
                failures.add(path + " missing visible h1");
            }

            if (hasFaqJsonLd && !hasVisibleFaq) {
                failures.add(path + " has FAQPage JSON-LD without visible FAQ section");
            }

            if (robots != null && robots.contains("noindex") && canonical.isBlank()) {
                failures.add(path + " is noindex but canonical missing");
            }

            Map<String, String> row = new LinkedHashMap<>();
            row.put("url", absoluteUrl);
            row.put("titleLength", String.valueOf(title.length()));
            row.put("descriptionLength", String.valueOf(description.length()));
            row.put("hasH1", String.valueOf(!h1.isBlank()));
            row.put("hasFaqJsonLd", String.valueOf(hasFaqJsonLd));
            row.put("hasVisibleFaq", String.valueOf(hasVisibleFaq));
            row.put("robots", robots == null ? "" : robots);
            row.put("canonical", canonical);
            rows.add(row);
        }

        writeReport(topUrls.size(), rows, warnings, failures);
        assertTrue(failures.isEmpty(), "Organic monitoring failures:\n - " + String.join("\n - ", failures));
    }

    @Test
    public void nonIndexablePagesShouldPointCanonicalToParent() throws Exception {
        assertCanonicalAndNoIndex("/salary/uber/jacksonville", AppConstants.BASE_URL + "/salary/uber");
        assertCanonicalAndNoIndex("/salary/uber/jacksonville/part-time",
                AppConstants.BASE_URL + "/salary/uber/jacksonville");
    }

    @Test
    public void userSubmittedWorkLevelPageShouldExposeEditorialReviewLabel() throws Exception {
        MvcResult result = mockMvc.perform(get("/salary/uber/miami/side-hustle"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertTrue(html.contains("Editorial review:"),
                "Work-level page should expose editorial review status");
        assertTrue(html.contains("Editorial review pending (user-submitted source)"),
                "User-submitted content should be labeled as pending editorial review");
    }

    @Test
    public void uberCoverageIntentPageShouldBridgeOfficialDirectoryAndCityReports() throws Exception {
        MvcResult result = mockMvc.perform(get("/uber/where-you-can-drive"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertTrue(html.contains("Where You Can Drive for Uber in the US"),
                "Coverage guide should expose a clear intent-matched H1");
        assertTrue(html.contains("https://www.uber.com/us/en/e/drive/cities/"),
                "Coverage guide should point to Uber's official city directory");
        assertTrue(html.contains("/salary/uber"),
                "Coverage guide should connect users back to Uber pay reports");
        assertTrue(html.contains("open_official_uber_coverage_source"),
                "Coverage guide should track official-directory CTA clicks");
    }

    @Test
    public void keyPagesShouldRenderValidJsonLdScripts() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Pattern jsonLdPattern = Pattern.compile(
                "<script\\s+type=\"application/ld\\+json\">\\s*(.*?)\\s*</script>",
                Pattern.DOTALL);

        List<String> paths = List.of(
                "/uber/where-you-can-drive",
                "/reports/uber-driver-hourly-earnings-2026",
                "/reports/doordash-driver-hourly-pay-2026",
                "/reports/doordash-driver-shift-evidence-2026",
                "/doordash/how-much-can-you-make-in-3-hours",
                "/doordash/how-much-can-you-make-in-a-day",
                "/doordash/can-you-make-100-a-day",
                "/doordash/after-gas",
                "/doordash/earnings-calculator",
                "/doordash/gas-calculator",
                "/doordash/mileage-deduction-calculator",
                "/uber/pay-calculator",
                "/uber/income-calculator",
                "/uber/tlc-pay-calculator",
                "/best-cities/doordash",
                "/salary/doordash/denver",
                "/salary/doordash/denver/side-hustle",
                "/salary/doordash/denver/after-gas",
                "/salary/doordash/denver/hourly-pay");
        List<String> failures = new ArrayList<>();

        for (String path : paths) {
            MvcResult result = mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andReturn();

            String html = result.getResponse().getContentAsString();
            Matcher matcher = jsonLdPattern.matcher(html);
            int scriptCount = 0;
            while (matcher.find()) {
                scriptCount++;
                String jsonLd = matcher.group(1).trim();
                try {
                    objectMapper.readTree(jsonLd);
                } catch (Exception e) {
                    failures.add(path + " script#" + scriptCount + " invalid JSON-LD: " + e.getMessage());
                }
            }

            if (scriptCount == 0) {
                failures.add(path + " missing JSON-LD script");
            }
        }

        assertTrue(failures.isEmpty(), "JSON-LD validation failures:\n - " + String.join("\n - ", failures));
    }

    @Test
    public void keywordAliasUrlsShouldPermanentlyRedirectToCanonicalPages() throws Exception {
        mockMvc.perform(get("/doordash/100-dollars-a-day"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/can-you-make-100-a-day"));
        mockMvc.perform(get("/doordash/does-doordash-pay-for-gas"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/after-gas"));
        mockMvc.perform(get("/doordash/how-much-does-doordash-pay-per-mile"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/pay-per-mile"));
        mockMvc.perform(get("/doordash/best-time-to-doordash"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/how-much-can-you-make-in-a-day"));
        mockMvc.perform(get("/uber/after-gas"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/uber-after-expenses"));
        mockMvc.perform(get("/uber/can-you-make-100-a-day"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/salary/uber/chicago/100-a-day"));
        mockMvc.perform(get("/uber/pay-per-mile"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/salary/uber/chicago/per-mile"));
        mockMvc.perform(get("/doordash/doordash-calculator"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/earnings-calculator"));
        mockMvc.perform(get("/doordash/gas-cost-calculator"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/gas-calculator"));
        mockMvc.perform(get("/doordash/irs-mileage-calculator"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/doordash/mileage-deduction-calculator"));
        mockMvc.perform(get("/uber/driver-pay-calculator"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/uber/pay-calculator"));
        mockMvc.perform(get("/uber/salary-calculator"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/uber/income-calculator"));
        mockMvc.perform(get("/tlc-pay-calculator"))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl("/uber/tlc-pay-calculator"));
    }

    @Test
    public void calculatorIntentPagesShouldMatchGscQueriesAndLinkIntoTools() throws Exception {
        MvcResult doordashResult = mockMvc.perform(get("/doordash/earnings-calculator"))
                .andExpect(status().isOk())
                .andReturn();
        Document doordashDoc = Jsoup.parse(doordashResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/doordash/earnings-calculator");
        assertTrue(doordashDoc.title().contains("DoorDash Earnings Calculator"),
                "DoorDash calculator page should target the exact earnings calculator query");
        assertTrue(firstH1(doordashDoc).contains("DoorDash Earnings Calculator"),
                "DoorDash calculator page should expose query-matched H1");
        assertTrue(doordashDoc.html().contains("/doordash?gross="),
                "DoorDash calculator page should link to a prefilled calculator");
        assertTrue(doordashDoc.html().contains("/doordash/gas-calculator"),
                "DoorDash calculator page should link adjacent calculator intent");

        MvcResult uberResult = mockMvc.perform(get("/uber/pay-calculator"))
                .andExpect(status().isOk())
                .andReturn();
        Document uberDoc = Jsoup.parse(uberResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/uber/pay-calculator");
        assertTrue(uberDoc.title().contains("Uber Pay Calculator"),
                "Uber pay calculator page should target the exact pay calculator query");
        assertTrue(firstH1(uberDoc).contains("Uber Pay Calculator"),
                "Uber pay calculator page should expose query-matched H1");
        assertTrue(uberDoc.html().contains("/uber?gross="),
                "Uber pay calculator page should link to a prefilled calculator");
        assertTrue(uberDoc.text().contains("Frequently Asked Questions"),
                "Calculator intent pages should render visible FAQ for FAQPage schema");
    }

    @Test
    public void sitemapShouldStageCoreReportsCityAndLongtailUrls() throws Exception {
        String sitemap = mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(sitemap.contains("<urlset"), "Root sitemap should expose page URLs directly for GSC");
        assertTrue(sitemap.contains("/doordash/earnings-calculator"),
                "Root sitemap should include high-intent calculator pages");
        assertTrue(sitemap.contains("/uber/pay-calculator"),
                "Root sitemap should include Uber calculator intent pages");
        assertTrue(sitemap.contains("/reports/doordash-driver-shift-evidence-2026"),
                "Root sitemap should include evidence reports");
        assertTrue(sitemap.contains("/salary/doordash/denver/after-gas"),
                "Root sitemap should include city intent pages");

        String sitemapIndex = mockMvc.perform(get("/sitemap-index.xml"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(sitemapIndex.contains("/sitemap-core.xml"), "Sitemap index should include core sitemap");
        assertTrue(sitemapIndex.contains("/sitemap-reports.xml"), "Sitemap index should include reports sitemap");
        assertTrue(sitemapIndex.contains("/sitemap-city.xml"), "Sitemap index should include city sitemap");
        assertTrue(sitemapIndex.contains("/sitemap-longtail.xml"), "Sitemap index should include longtail sitemap");

        String core = mockMvc.perform(get("/sitemap-core.xml"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(core.contains("/doordash/earnings-calculator"),
                "Core sitemap should include high-intent calculator pages");
        assertTrue(core.contains("/uber/pay-calculator"),
                "Core sitemap should include Uber calculator intent pages");
        assertTrue(!core.contains("<priority>") && !core.contains("<changefreq>"),
                "Sitemaps should not emit ignored priority/changefreq hints");

        String city = mockMvc.perform(get("/sitemap-city.xml"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(city.contains("<lastmod>2026-07-06</lastmod>"),
                "City sitemap should keep a city-data lastmod instead of marking every URL fresh today");
    }

    @Test
    public void driverReportFormShouldPostToInternalReviewQueue() throws Exception {
        mockMvc.perform(post("/driver-reports/submit")
                .param("source_page", "city_report")
                .param("source_path", "/salary/doordash/denver")
                .param("app", "doordash")
                .param("app_name", "DoorDash")
                .param("city", "Denver")
                .param("city_slug", "denver")
                .param("modeled_net_hourly", "18.42")
                .param("weekly_gross", "560")
                .param("weekly_miles", "210")
                .param("weekly_hours", "24")
                .param("driver_note", "Dinner shifts had long waits."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/driver-report-submitted"));

        MvcResult result = mockMvc.perform(get("/driver-report-submitted"))
                .andExpect(status().isOk())
                .andReturn();
        Document doc = Jsoup.parse(result.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/driver-report-submitted");
        assertTrue(metaContent(doc, "robots").contains("noindex"),
                "Submitted page should stay noindex");
        assertTrue(doc.text().contains("queued for editorial review"),
                "Submitted page should explain the review queue");
    }

    @Test
    public void cityReportShouldExposeTrackedHeroActions() throws Exception {
        MvcResult result = mockMvc.perform(get("/salary/uber/miami"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertTrue(html.contains("Adjust this city estimate"),
                "City report hero should expose a prefilled calculator CTA");
        assertTrue(html.contains("/uber?gross="),
                "City report should link to the app calculator with prefilled query params");
        assertTrue(html.contains("Estimated Net Earnings Result"),
                "City report hero should lead with the earnings result, not a generic calculator intro");
        assertTrue(html.contains("\"@type\":\"WebApplication\""),
                "City report JSON-LD should expose calculator/tool identity");
        assertTrue(html.contains("estimate_quarterly_taxes"),
                "City report hero should expose a tracked tax estimator CTA");
        assertTrue(html.contains("compare_best_cities"),
                "City report hero should expose a tracked best-cities CTA");
        assertTrue(html.contains("Driver field notes"),
                "City report should expose source-backed driver field notes");
        assertTrue(html.contains("Published shift evidence"),
                "City report should expose reviewed public shift evidence");
        assertTrue(html.contains("Real Uber shift signals"),
                "City report should frame shift evidence as real-world checks");
        assertTrue(html.contains("NerdWallet Uber pay test"),
                "City report field notes should include app-specific field-test evidence");
        assertTrue(html.contains("Top-result comparison"),
                "City report should explain how it competes with ranking result types");
        assertTrue(html.contains("Official pay docs"),
                "City report competitor section should include official pay docs as a search-result pattern");
        assertTrue(html.contains("Usually missing:"),
                "City report competitor section should state the missing check GigVerdict answers");
        assertTrue(html.contains("Share a real Uber shift in Miami"),
                "City report should collect first-party driver reports");
        assertTrue(html.contains("name=\"lead_type\" value=\"driver_earnings_report\""),
                "Driver report form should identify report submissions");
        assertTrue(html.contains("name=\"source_path\" value=\"/salary/uber/miami\""),
                "Driver report form should preserve source URL context");
    }

    @Test
    public void priorityCityPagesShouldExposeCitySpecificDriverEvidence() throws Exception {
        MvcResult denverResult = mockMvc.perform(get("/salary/doordash/denver/side-hustle"))
                .andExpect(status().isOk())
                .andReturn();
        String denverHtml = denverResult.getResponse().getContentAsString();
        assertTrue(denverHtml.contains("Denver pattern"),
                "Denver work-level page should expose city-specific DoorDash evidence");
        assertTrue(denverHtml.contains("Denver DoorDash driver discussion"),
                "Denver work-level page should link the city-specific source");
        assertTrue(denverHtml.contains("Published shift evidence"),
                "Denver work-level page should expose shift evidence");
        assertTrue(denverHtml.contains("$12-$15/hr around Centennial-style suburban work"),
                "Denver work-level page should expose the reported local earnings range");
        assertTrue(denverHtml.contains("Share a real DoorDash shift in Denver"),
                "Work-level page should collect first-party driver reports");
        assertTrue(denverHtml.contains("name=\"source_path\" value=\"/salary/doordash/denver/side-hustle\""),
                "Work-level driver report form should preserve source URL context");

        MvcResult chicagoResult = mockMvc.perform(get("/salary/uber/chicago"))
                .andExpect(status().isOk())
                .andReturn();
        String chicagoHtml = chicagoResult.getResponse().getContentAsString();
        assertTrue(chicagoHtml.contains("Chicago pattern"),
                "Chicago city page should expose city-specific Uber evidence");
        assertTrue(chicagoHtml.contains("AskChicago Uber/Lyft summer thread"),
                "Chicago city page should link the city-specific source");

        MvcResult phoenixResult = mockMvc.perform(get("/salary/doordash/phoenix/100-a-day"))
                .andExpect(status().isOk())
                .andReturn();
        String phoenixHtml = phoenixResult.getResponse().getContentAsString();
        assertTrue(phoenixHtml.contains("Phoenix pattern"),
                "Phoenix daily-target page should expose Phoenix-specific DoorDash evidence");
        assertTrue(phoenixHtml.contains("Phoenix DoorDash slowdown discussion"),
                "Phoenix daily-target page should cite the city-specific slowdown source");

        MvcResult seattleResult = mockMvc.perform(get("/salary/uber/seattle"))
                .andExpect(status().isOk())
                .andReturn();
        String seattleHtml = seattleResult.getResponse().getContentAsString();
        assertTrue(seattleHtml.contains("Seattle pattern"),
                "Seattle city page should expose Seattle-specific Uber evidence");
        assertTrue(seattleHtml.contains("Seattle Uber weekly pay discussion"),
                "Seattle city page should cite the city-specific Uber source");

        MvcResult sanJoseResult = mockMvc.perform(get("/salary/doordash/san-jose"))
                .andExpect(status().isOk())
                .andReturn();
        String sanJoseHtml = sanJoseResult.getResponse().getContentAsString();
        assertTrue(sanJoseHtml.contains("San Jose pattern"),
                "San Jose city page should expose California active-time evidence");
        assertTrue(sanJoseHtml.contains("San Jose Prop 22 driver discussion"),
                "San Jose city page should cite the Prop 22 source");
    }

    @Test
    public void priorityRecoveryUrlsShouldHaveStrongInternalLinks() throws Exception {
        MvcResult appHubResult = mockMvc.perform(get("/salary/doordash"))
                .andExpect(status().isOk())
                .andReturn();
        String appHubHtml = appHubResult.getResponse().getContentAsString();
        assertTrue(appHubHtml.contains("/salary/doordash/denver/side-hustle"),
                "DoorDash app hub should directly link the Denver side-hustle recovery URL");
        assertTrue(appHubHtml.contains("/salary/doordash/phoenix/100-a-day"),
                "DoorDash app hub should directly link daily target intent URLs");
        assertTrue(appHubHtml.contains("/salary/doordash/denver/hourly-pay"),
                "DoorDash app hub should directly link city hourly-pay query URLs");
        assertTrue(appHubHtml.contains("/salary/doordash/phoenix/how-much-can-you-make"),
                "DoorDash app hub should directly link how-much-can-you-make query URLs");
        assertTrue(appHubHtml.contains("/salary/doordash/dallas/best-areas"),
                "DoorDash app hub should directly link best-area query URLs");
        assertTrue(appHubHtml.contains("/salary/doordash/chicago/uber-eats-vs-doordash"),
                "DoorDash app hub should directly link delivery-app comparison URLs");
        assertTrue(appHubHtml.contains("Goal-based searches"),
                "DoorDash app hub should expose goal-based pSEO links");
        assertTrue(appHubHtml.contains("/reports/doordash-driver-hourly-pay-2026"),
                "DoorDash app hub should link the DoorDash hourly pay report");
        assertTrue(appHubHtml.contains("/reports/doordash-driver-shift-evidence-2026"),
                "DoorDash app hub should link the DoorDash shift evidence hub");
        assertTrue(appHubHtml.contains("/doordash/how-much-can-you-make-in-3-hours"),
                "DoorDash app hub should link short duration earning pages");
        assertTrue(appHubHtml.contains("/doordash/how-much-can-you-make-in-8-hours"),
                "DoorDash app hub should link full-day duration earning pages");
        assertTrue(appHubHtml.contains("/doordash/how-much-can-you-make-in-a-day"),
                "DoorDash app hub should link daily duration earning pages");
        assertTrue(appHubHtml.contains("/doordash/can-you-make-100-a-day"),
                "DoorDash app hub should link $100/day money-intent pages");
        assertTrue(appHubHtml.contains("/doordash/can-you-make-200-a-day"),
                "DoorDash app hub should link $200/day money-intent pages");
        assertTrue(appHubHtml.contains("/doordash/after-gas"),
                "DoorDash app hub should link after-gas money-intent pages");
        assertTrue(appHubHtml.contains("/doordash/pay-per-mile"),
                "DoorDash app hub should link pay-per-mile money-intent pages");
        assertTrue(appHubHtml.contains("/salary/doordash/las-vegas/100-a-day"),
                "DoorDash app hub should link high-intent city target combinations");

        MvcResult uberHubResult = mockMvc.perform(get("/salary/uber"))
                .andExpect(status().isOk())
                .andReturn();
        String uberHubHtml = uberHubResult.getResponse().getContentAsString();
        assertTrue(uberHubHtml.contains("Uber driver hourly earnings Atlanta GA 2026"),
                "Uber app hub should directly reinforce the Atlanta hourly earnings quick-win query");
        assertTrue(uberHubHtml.contains("/reports/uber-driver-hourly-earnings-2026"),
                "Uber app hub should link the shareable hourly earnings report");
        assertTrue(uberHubHtml.contains("/salary/uber/houston"),
                "Uber app hub should link Houston hourly earnings quick-win page");
        assertTrue(uberHubHtml.contains("/salary/uber/orlando"),
                "Uber app hub should link Orlando earnings quick-win page");

        MvcResult directoryResult = mockMvc.perform(get("/salary/directory"))
                .andExpect(status().isOk())
                .andReturn();
        String directoryHtml = directoryResult.getResponse().getContentAsString();
        assertTrue(directoryHtml.contains("Priority earnings reports"),
                "Directory should expose priority earnings reports");
        assertTrue(directoryHtml.contains("/salary/uber/chicago"),
                "Directory should directly link the Chicago Uber recovery URL");
        assertTrue(directoryHtml.contains("/salary/uber/chicago/100-a-day"),
                "Directory should directly link daily target intent URLs");
        assertTrue(directoryHtml.contains("/salary/doordash/dallas/nights-weekends"),
                "Directory should directly link nights/weekends intent URLs");
        assertTrue(directoryHtml.contains("/salary/doordash/denver/hourly-pay"),
                "Directory should directly link DoorDash hourly-pay intent URLs");
        assertTrue(directoryHtml.contains("/salary/doordash/phoenix/how-much-can-you-make"),
                "Directory should directly link DoorDash how-much-can-you-make intent URLs");
        assertTrue(directoryHtml.contains("/salary/doordash/dallas/best-areas"),
                "Directory should directly link DoorDash best-area intent URLs");
        assertTrue(directoryHtml.contains("/salary/doordash/chicago/uber-eats-vs-doordash"),
                "Directory should directly link DoorDash app-comparison intent URLs");
        assertTrue(directoryHtml.contains("Hourly earnings quick wins"),
                "Directory should expose the hourly earnings quick-win cluster");
        assertTrue(directoryHtml.contains("/reports/uber-driver-hourly-earnings-2026"),
                "Directory should link the Uber hourly earnings report");
        assertTrue(directoryHtml.contains("/reports/doordash-driver-hourly-pay-2026"),
                "Directory should link the DoorDash hourly pay report");
        assertTrue(directoryHtml.contains("/reports/doordash-driver-shift-evidence-2026"),
                "Directory should link the DoorDash shift evidence hub");
        assertTrue(directoryHtml.contains("/doordash/how-much-can-you-make-in-3-hours"),
                "Directory should link DoorDash duration earning pages");
        assertTrue(directoryHtml.contains("/doordash/how-much-can-you-make-in-a-week"),
                "Directory should link DoorDash weekly earning pages");
        assertTrue(directoryHtml.contains("/doordash/how-much-can-you-make-in-a-day"),
                "Directory should link DoorDash daily earning pages");
        assertTrue(directoryHtml.contains("/doordash/can-you-make-100-a-day"),
                "Directory should link DoorDash $100/day money-intent pages");
        assertTrue(directoryHtml.contains("/doordash/can-you-make-200-a-day"),
                "Directory should link DoorDash $200/day money-intent pages");
        assertTrue(directoryHtml.contains("/doordash/after-gas"),
                "Directory should link DoorDash after-gas money-intent pages");
        assertTrue(directoryHtml.contains("/doordash/pay-per-mile"),
                "Directory should link DoorDash pay-per-mile money-intent pages");
        assertTrue(directoryHtml.contains("/salary/doordash/las-vegas/100-a-day"),
                "Directory should link high-intent city daily target combinations");
        assertTrue(directoryHtml.contains("Uber driver hourly earnings Atlanta GA 2026"),
                "Directory should reinforce the Atlanta hourly earnings anchor text");
        assertTrue(directoryHtml.contains("DoorDash driver hourly pay 2026 report"),
                "Directory should reinforce the DoorDash hourly pay report anchor text");
        assertTrue(directoryHtml.contains("Rideshare driver hourly earnings Houston TX"),
                "Directory should reinforce Houston rideshare hourly earnings anchor text");

        MvcResult bestCitiesResult = mockMvc.perform(get("/best-cities/doordash"))
                .andExpect(status().isOk())
                .andReturn();
        String bestCitiesHtml = bestCitiesResult.getResponse().getContentAsString();
        assertTrue(bestCitiesHtml.contains("/salary/doordash/denver/side-hustle"),
                "Best-cities table should link side-hustle detail pages");
        assertTrue(bestCitiesHtml.contains("/salary/doordash/denver/hourly-pay"),
                "Best-cities table should link DoorDash hourly-pay intent pages");
        assertTrue(bestCitiesHtml.contains("/salary/doordash/denver/best-areas"),
                "Best-cities table should link DoorDash best-area intent pages");
        assertTrue(bestCitiesHtml.contains("/reports/doordash-driver-shift-evidence-2026"),
                "Best-cities page should link the DoorDash shift evidence hub");
        assertTrue(bestCitiesHtml.contains("/doordash/can-you-make-100-a-day"),
                "Best-cities page should link DoorDash money-intent pages");
        assertTrue(bestCitiesHtml.contains("/doordash/after-gas"),
                "Best-cities page should link DoorDash after-gas pages");
        assertTrue(bestCitiesHtml.contains("/doordash/pay-per-mile"),
                "Best-cities page should link DoorDash pay-per-mile pages");
    }

    @Test
    public void cityIntentPagesShouldRenderLongTailEarningsAnswers() throws Exception {
        MvcResult afterGasResult = mockMvc.perform(get("/salary/doordash/denver/after-gas"))
                .andExpect(status().isOk())
                .andReturn();
        String afterGasHtml = afterGasResult.getResponse().getContentAsString();
        assertTrue(afterGasHtml.contains("DoorDash Denver After Gas"),
                "After-gas intent page should expose intent-matched H1 language");
        assertTrue(afterGasHtml.contains("Direct answer"),
                "City intent page should lead with a direct answer section");
        assertTrue(afterGasHtml.contains("Goal math"),
                "City intent page should expose target calculator math");
        assertTrue(afterGasHtml.contains("$200 net day"),
                "City intent page should include stretch-goal math");
        assertTrue(afterGasHtml.contains("Data-backed checks"),
                "City intent page should include intent-specific metric cards");
        assertTrue(afterGasHtml.contains("Estimated weekly fuel"),
                "After-gas page should include after-gas metric cards");
        assertTrue(afterGasHtml.contains("Evidence patterns"),
                "City intent page should include source-backed evidence-pattern cards");
        assertTrue(afterGasHtml.contains("IRS 2026 mileage rate"),
                "After-gas page should cite the 2026 mileage benchmark");
        assertTrue(afterGasHtml.contains("Driver field notes"),
                "City intent page should reuse driver field evidence");
        assertTrue(afterGasHtml.contains("Published shift evidence"),
                "City intent page should expose shift evidence");
        assertTrue(afterGasHtml.contains("$86 gross over 6.5 hours and 90 miles"),
                "City intent page should expose the DoorDash field-test shift benchmark");
        assertTrue(afterGasHtml.contains("Share a real DoorDash shift in Denver"),
                "City intent page should collect first-party driver reports");
        assertTrue(afterGasHtml.contains("name=\"source_page\" value=\"city_intent_after-gas\""),
                "City intent driver report form should preserve intent context");
        assertTrue(afterGasHtml.contains("/salary/doordash/denver/per-mile"),
                "City intent page should internally link related intent pages");

        MvcResult activeTimeResult = mockMvc.perform(get("/salary/uber/chicago/active-time"))
                .andExpect(status().isOk())
                .andReturn();
        String activeTimeHtml = activeTimeResult.getResponse().getContentAsString();
        assertTrue(activeTimeHtml.contains("Uber Chicago Active Time"),
                "Active-time intent page should expose intent-matched H1 language");
        assertTrue(activeTimeHtml.contains("Chicago pattern"),
                "Active-time intent page should include city-specific field evidence");
        assertTrue(activeTimeHtml.contains("All-in hourly stress test"),
                "Active-time page should include waiting-time stress-test metrics");
        assertTrue(activeTimeHtml.contains("Uber earnings guide"),
                "Active-time page should cite official platform clock/source material");

        MvcResult dailyTargetResult = mockMvc.perform(get("/salary/doordash/phoenix/100-a-day"))
                .andExpect(status().isOk())
                .andReturn();
        String dailyTargetHtml = dailyTargetResult.getResponse().getContentAsString();
        assertTrue(dailyTargetHtml.contains("DoorDash Phoenix $100 a Day"),
                "Daily target intent page should expose target-matched H1 language");
        assertTrue(dailyTargetHtml.contains("Hours to $100 net"),
                "Daily target intent page should expose target-hour metric cards");
        assertTrue(dailyTargetHtml.contains("DoorDash $100/day discussion"),
                "Daily target intent page should cite target-specific driver discussion");
        assertTrue(dailyTargetHtml.contains("Top-result comparison"),
                "Daily target page should show the competitor result-pattern comparison");
        assertTrue(dailyTargetHtml.contains("Creator SERP"),
                "Daily target page should compare against creator-style target searches");
        assertTrue(dailyTargetHtml.contains("Gridwise DoorDash pay data"),
                "Daily target page should include the large-dataset competitor pattern");
        assertTrue(dailyTargetHtml.contains("/salary/doordash/las-vegas/100-a-day"),
                "Daily target page should link adjacent high-intent city combinations");

        MvcResult hourlyPayResult = mockMvc.perform(get("/salary/doordash/denver/hourly-pay"))
                .andExpect(status().isOk())
                .andReturn();
        String hourlyPayHtml = hourlyPayResult.getResponse().getContentAsString();
        assertTrue(hourlyPayHtml.contains("DoorDash Denver Hourly Pay"),
                "Hourly-pay intent page should expose query-matched H1 language");
        assertTrue(hourlyPayHtml.contains("Gross hourly"),
                "Hourly-pay intent page should expose hourly-specific metrics");
        assertTrue(hourlyPayHtml.contains("Published shift evidence"),
                "Hourly-pay intent page should expose public shift evidence");
        assertTrue(hourlyPayHtml.contains("$86 gross over 6.5 hours and 90 miles"),
                "Hourly-pay intent page should expose the DoorDash field-test shift benchmark");
        assertTrue(hourlyPayHtml.contains("/salary/doordash/denver/how-much-can-you-make"),
                "Hourly-pay intent page should internally link the adjacent how-much query page");

        MvcResult howMuchResult = mockMvc.perform(get("/salary/doordash/phoenix/how-much-can-you-make"))
                .andExpect(status().isOk())
                .andReturn();
        String howMuchHtml = howMuchResult.getResponse().getContentAsString();
        assertTrue(howMuchHtml.contains("DoorDash Phoenix How Much Can You Make"),
                "How-much intent page should expose query-matched H1 language");
        assertTrue(howMuchHtml.contains("Weekly net estimate"),
                "How-much intent page should expose weekly earning metrics");
        assertTrue(howMuchHtml.contains("how much can you make with DoorDash in Phoenix"),
                "How-much intent page should include the exact query-style answer phrase");

        MvcResult bestAreasResult = mockMvc.perform(get("/salary/doordash/dallas/best-areas"))
                .andExpect(status().isOk())
                .andReturn();
        String bestAreasHtml = bestAreasResult.getResponse().getContentAsString();
        assertTrue(bestAreasHtml.contains("DoorDash Dallas Best Areas"),
                "Best-areas intent page should expose query-matched H1 language");
        assertTrue(bestAreasHtml.contains("Primary demand zone"),
                "Best-areas intent page should expose zone-selection metrics");
        assertTrue(bestAreasHtml.contains("best areas to DoorDash in Dallas"),
                "Best-areas intent page should include the query-style answer phrase");

        MvcResult appComparisonResult = mockMvc.perform(get("/salary/doordash/chicago/uber-eats-vs-doordash"))
                .andExpect(status().isOk())
                .andReturn();
        String appComparisonHtml = appComparisonResult.getResponse().getContentAsString();
        assertTrue(appComparisonHtml.contains("Uber Eats vs DoorDash in Chicago"),
                "App-comparison intent page should expose comparison H1 language");
        assertTrue(appComparisonHtml.contains("DoorDash net hourly"),
                "App-comparison page should show DoorDash-specific comparison metrics");
        assertTrue(appComparisonHtml.contains("Uber model net hourly"),
                "App-comparison page should show the comparison app metric");

        MvcResult compareResult = mockMvc.perform(get("/compare/chicago/uber-vs-doordash"))
                .andExpect(status().isOk())
                .andReturn();
        Document compareDoc = Jsoup.parse(compareResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/compare/chicago/uber-vs-doordash");
        assertTrue(compareDoc.title().contains("Uber Eats vs DoorDash Chicago"),
                "Standalone compare page title should be concise enough for CTR testing");
        assertTrue(compareDoc.text().contains("Quick answer: Uber models"),
                "Standalone compare page should expose a numeric quick answer near the top");
        assertTrue(compareDoc.text().contains("Evidence profile"),
                "Standalone compare page should expose an evidence profile");

        mockMvc.perform(get("/salary/uber/chicago/uber-eats-vs-doordash"))
                .andExpect(status().isNotFound());

        MvcResult monthlyTargetResult = mockMvc.perform(get("/salary/uber/los-angeles/1000-a-month"))
                .andExpect(status().isOk())
                .andReturn();
        String monthlyTargetHtml = monthlyTargetResult.getResponse().getContentAsString();
        assertTrue(monthlyTargetHtml.contains("Uber Los Angeles $1,000 a Month"),
                "Monthly target intent page should expose monthly target H1 language");
        assertTrue(monthlyTargetHtml.contains("Weekly net target"),
                "Monthly target intent page should expose weekly target metric cards");

        MvcResult bestHoursResult = mockMvc.perform(get("/salary/doordash/dallas/nights-weekends"))
                .andExpect(status().isOk())
                .andReturn();
        String bestHoursHtml = bestHoursResult.getResponse().getContentAsString();
        assertTrue(bestHoursHtml.contains("Best Hours to DoorDash in Dallas"),
                "Dallas nights/weekends page should target best-hours DoorDash searches");
        assertTrue(bestHoursHtml.contains("The best hours to DoorDash in Dallas"),
                "Dallas best-hours page should answer the query directly");
    }

    @Test
    public void prioritySeoPagesShouldExposeEarningsFirstLanguage() throws Exception {
        MvcResult cityResult = mockMvc.perform(get("/salary/doordash/phoenix"))
                .andExpect(status().isOk())
                .andReturn();
        Document cityDoc = Jsoup.parse(cityResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/salary/doordash/phoenix");
        assertTrue(cityDoc.title().contains("Driver Earnings"),
                "Priority city page title should lead with earnings framing");
        assertTrue(firstH1(cityDoc).contains("Driver Earnings"),
                "Priority city page H1 should lead with earnings framing");

        MvcResult uberQuickWinResult = mockMvc.perform(get("/salary/uber/atlanta"))
                .andExpect(status().isOk())
                .andReturn();
        Document uberQuickWinDoc = Jsoup.parse(uberQuickWinResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/salary/uber/atlanta");
        assertTrue(uberQuickWinDoc.title().contains("Hourly"),
                "Uber Atlanta title should target hourly earnings quick-win queries");
        assertTrue(firstH1(uberQuickWinDoc).contains("Driver Earnings"),
                "Uber Atlanta H1 should preserve earnings-first language");
        assertTrue(uberQuickWinDoc.text().contains("Priority hourly earnings cluster"),
                "Uber Atlanta should expose the hourly earnings query cluster");
        assertTrue(uberQuickWinDoc.text().contains("Uber driver hourly earnings Atlanta GA 2025 2026"),
                "Uber Atlanta should include the exact GSC quick-win query phrase");
        assertTrue(uberQuickWinDoc.html().contains("/salary/uber/atlanta/after-gas"),
                "Uber Atlanta should link from city page into the after-gas intent page");

        MvcResult reportResult = mockMvc.perform(get("/reports/uber-driver-hourly-earnings-2026"))
                .andExpect(status().isOk())
                .andReturn();
        Document reportDoc = Jsoup.parse(reportResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/reports/uber-driver-hourly-earnings-2026");
        assertTrue(reportDoc.title().contains("Uber Driver Hourly Earnings 2026"),
                "Hourly earnings report should target the broad Uber 2026 query");
        assertTrue(firstH1(reportDoc).contains("Uber Driver Hourly Earnings 2026"),
                "Hourly earnings report should expose a query-matched H1");
        assertTrue(reportDoc.text().contains("uber driver hourly earnings atlanta ga 2025 2026"),
                "Hourly earnings report should include the strongest GSC quick-win query");
        assertTrue(reportDoc.html().contains("/salary/uber/atlanta"),
                "Hourly earnings report should link into Atlanta city report");
        assertTrue(reportDoc.html().contains("/salary/uber/orlando/100-a-day"),
                "Hourly earnings report should link into Orlando daily-target report");
        assertTrue(reportDoc.text().contains("Suggested citation"),
                "Hourly earnings report should include a shareable citation block");

        MvcResult doordashReportResult = mockMvc.perform(get("/reports/doordash-driver-hourly-pay-2026"))
                .andExpect(status().isOk())
                .andReturn();
        Document doordashReportDoc = Jsoup.parse(doordashReportResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/reports/doordash-driver-hourly-pay-2026");
        assertTrue(doordashReportDoc.title().contains("DoorDash Driver Hourly Pay 2026"),
                "DoorDash hourly pay report should target broad 2026 pay queries");
        assertTrue(firstH1(doordashReportDoc).contains("DoorDash Driver Hourly Pay 2026"),
                "DoorDash hourly pay report should expose a query-matched H1");
        assertTrue(doordashReportDoc.text().contains("average doordash earnings per hour 2025 2026"),
                "DoorDash hourly pay report should include the GSC query phrase");
        assertTrue(doordashReportDoc.text().contains("Published shift evidence"),
                "DoorDash hourly pay report should expose public shift evidence");
        assertTrue(doordashReportDoc.text().contains("$86 gross over 6.5 hours and 90 miles"),
                "DoorDash hourly pay report should expose the field-test shift benchmark");
        assertTrue(doordashReportDoc.html().contains("/best-cities/doordash"),
                "DoorDash hourly pay report should bridge into the city ranking");
        assertTrue(doordashReportDoc.html().contains("/salary/doordash/phoenix/100-a-day"),
                "DoorDash hourly pay report should link into daily-target city reports");
        assertTrue(doordashReportDoc.html().contains("/salary/doordash/denver/hourly-pay"),
                "DoorDash hourly pay report should link into city hourly-pay intent reports");
        assertTrue(doordashReportDoc.html().contains("/salary/doordash/phoenix/how-much-can-you-make"),
                "DoorDash hourly pay report should link into how-much-can-you-make intent reports");
        assertTrue(doordashReportDoc.html().contains("/salary/doordash/dallas/best-areas"),
                "DoorDash hourly pay report should link into best-area intent reports");
        assertTrue(doordashReportDoc.html().contains("/salary/doordash/chicago/uber-eats-vs-doordash"),
                "DoorDash hourly pay report should link into app-comparison intent reports");
        assertTrue(doordashReportDoc.html().contains("/reports/doordash-driver-shift-evidence-2026"),
                "DoorDash hourly pay report should link into the shift evidence hub");
        assertTrue(doordashReportDoc.html().contains("/doordash/how-much-can-you-make-in-3-hours"),
                "DoorDash hourly pay report should link into duration earning pages");
        assertTrue(doordashReportDoc.html().contains("/doordash/can-you-make-100-a-day"),
                "DoorDash hourly pay report should link into money-intent pages");
        assertTrue(doordashReportDoc.html().contains("/doordash/after-gas"),
                "DoorDash hourly pay report should link into after-gas money-intent pages");
        assertTrue(doordashReportDoc.html().contains("/doordash/pay-per-mile"),
                "DoorDash hourly pay report should link into pay-per-mile money-intent pages");

        MvcResult evidenceHubResult = mockMvc.perform(get("/reports/doordash-driver-shift-evidence-2026"))
                .andExpect(status().isOk())
                .andReturn();
        Document evidenceHubDoc = Jsoup.parse(evidenceHubResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/reports/doordash-driver-shift-evidence-2026");
        assertTrue(evidenceHubDoc.title().contains("DoorDash Driver Shift Evidence 2026"),
                "Shift evidence hub should target the evidence dataset query");
        assertTrue(firstH1(evidenceHubDoc).contains("DoorDash Driver Shift Evidence 2026"),
                "Shift evidence hub should expose a query-matched H1");
        assertTrue(evidenceHubDoc.text().contains("Modeled vs reported"),
                "Shift evidence hub should separate modeled estimates from reported evidence");
        assertTrue(evidenceHubDoc.text().contains("Gross, Miles, Active Time, Net Checks"),
                "Shift evidence hub should expose the table dimensions in page copy");
        assertTrue(evidenceHubDoc.html().contains("/doordash/how-much-can-you-make-in-3-hours"),
                "Shift evidence hub should link into duration earning pages");
        assertTrue(evidenceHubDoc.html().contains("/doordash/can-you-make-100-a-day"),
                "Shift evidence hub should link into $100/day money-intent pages");
        assertTrue(evidenceHubDoc.html().contains("/doordash/after-gas"),
                "Shift evidence hub should link into after-gas money-intent pages");

        MvcResult durationResult = mockMvc.perform(get("/doordash/how-much-can-you-make-in-3-hours"))
                .andExpect(status().isOk())
                .andReturn();
        Document durationDoc = Jsoup.parse(durationResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/doordash/how-much-can-you-make-in-3-hours");
        assertTrue(durationDoc.title().contains("How Much Can You Make with DoorDash in 3 Hours"),
                "Duration page should target the exact earning-duration query");
        assertTrue(firstH1(durationDoc).contains("How Much Can You Make with DoorDash in 3 Hours"),
                "Duration page should expose query-matched H1 language");
        assertTrue(durationDoc.text().contains("$35-$60 Net Estimate"),
                "Duration page should answer with a net pay range above the fold");
        assertTrue(durationDoc.html().contains("/doordash/how-much-can-you-make-in-8-hours"),
                "Duration page should link adjacent duration earning pages");
        assertTrue(durationDoc.text().contains("Goal stress test"),
                "Duration page should expose target feasibility checks");
        assertTrue(durationDoc.html().contains("/doordash/can-you-make-100-a-day"),
                "Duration page should link into money-intent pages");

        MvcResult dailyDurationResult = mockMvc.perform(get("/doordash/how-much-can-you-make-in-a-day"))
                .andExpect(status().isOk())
                .andReturn();
        Document dailyDurationDoc = Jsoup.parse(dailyDurationResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/doordash/how-much-can-you-make-in-a-day");
        assertTrue(dailyDurationDoc.title().contains("How Much Can You Make with DoorDash in a Day"),
                "Daily duration page should target in-a-day earnings searches");
        assertTrue(dailyDurationDoc.text().contains("$95-$165 Net Estimate"),
                "Daily duration page should expose the full-day net range");

        MvcResult moneyIntentResult = mockMvc.perform(get("/doordash/can-you-make-100-a-day"))
                .andExpect(status().isOk())
                .andReturn();
        Document moneyIntentDoc = Jsoup.parse(moneyIntentResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/doordash/can-you-make-100-a-day");
        assertTrue(moneyIntentDoc.title().contains("Can You Make $100 a Day with DoorDash"),
                "Money-intent page should target the exact $100/day query");
        assertTrue(moneyIntentDoc.title().contains("Real Hours + Miles"),
                "Money-intent page title should use CTR-focused hours and miles language");
        assertTrue(firstH1(moneyIntentDoc).contains("Can You Make $100 a Day with DoorDash"),
                "Money-intent page should expose query-matched H1 language");
        assertTrue(moneyIntentDoc.text().contains("DoorDash money-intent calculator table"),
                "Money-intent page should expose gross/net/hour/mile math");
        assertTrue(moneyIntentDoc.html().contains("/salary/doordash/las-vegas/100-a-day"),
                "Money-intent page should link city-specific target combinations");
        assertTrue(moneyIntentDoc.html().contains("/doordash/pay-per-mile"),
                "Money-intent page should link adjacent pay-per-mile page");
        assertTrue(moneyIntentDoc.text().contains("Frequently Asked Questions"),
                "Money-intent page should render visible FAQ for its FAQPage schema");

        MvcResult afterGasMoneyResult = mockMvc.perform(get("/doordash/after-gas"))
                .andExpect(status().isOk())
                .andReturn();
        Document afterGasMoneyDoc = Jsoup.parse(afterGasMoneyResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/doordash/after-gas");
        assertTrue(afterGasMoneyDoc.title().contains("What Drivers Actually Keep"),
                "After-gas page title should use CTR-focused take-home language");

        MvcResult perMileMoneyResult = mockMvc.perform(get("/doordash/pay-per-mile"))
                .andExpect(status().isOk())
                .andReturn();
        Document perMileMoneyDoc = Jsoup.parse(perMileMoneyResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/doordash/pay-per-mile");
        assertTrue(perMileMoneyDoc.title().contains("The Offer Floor That Matters"),
                "Pay-per-mile page title should use CTR-focused offer-floor language");

        MvcResult workLevelResult = mockMvc.perform(get("/salary/doordash/phoenix/side-hustle"))
                .andExpect(status().isOk())
                .andReturn();
        String workLevelHtml = workLevelResult.getResponse().getContentAsString();
        assertTrue(workLevelHtml.contains("DoorDash Driver Earnings in Phoenix for Side-Hustle"),
                "Priority work-level page should expose earnings framing");
        assertTrue(workLevelHtml.contains("open_prefilled_calculator"),
                "Priority work-level page should link into the prefilled calculator");

        MvcResult bestCitiesResult = mockMvc.perform(get("/best-cities/doordash"))
                .andExpect(status().isOk())
                .andReturn();
        Document bestCitiesDoc = Jsoup.parse(bestCitiesResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/best-cities/doordash");
        assertTrue(bestCitiesDoc.title().contains("Best Cities for DoorDash Drivers"),
                "Best-cities page title should target driver city queries");
        assertTrue(bestCitiesDoc.title().contains("Highest Net Pay"),
                "DoorDash best-cities title should target click-driven dash-city queries");
        assertTrue(firstH1(bestCitiesDoc).contains("Best Cities for DoorDash Drivers"),
                "Best-cities page H1 should target best-city driver intent");
        assertTrue(bestCitiesDoc.text().contains("highest paying DoorDash cities near me"),
                "DoorDash best-cities page should answer near-me earnings ranking intent");
        assertTrue(bestCitiesDoc.text().contains("Best cities to DoorDash near me"),
                "DoorDash best-cities page should expose query-matched guidance");
        assertTrue(bestCitiesDoc.text().contains("DoorDash city pay dataset"),
                "DoorDash best-cities page should expose dataset framing");
        assertTrue(bestCitiesDoc.text().contains("Hours to $100"),
                "DoorDash best-cities page should expose daily target math");
        assertTrue(bestCitiesDoc.text().contains("20-hour weekly net"),
                "DoorDash best-cities page should expose weekly net math");
        assertTrue(bestCitiesDoc.text().contains("local demand signals"),
                "DoorDash best-cities page should expose city-specific demand signals");
        assertTrue(bestCitiesDoc.text().contains("Published shift evidence"),
                "DoorDash best-cities page should expose public shift evidence");
        assertTrue(bestCitiesDoc.text().contains("Modeled net check from reported gross + miles"),
                "DoorDash best-cities page should show reported-shift net checks when miles are available");
        assertTrue(bestCitiesDoc.html().contains("id=\"ranking-table\""),
                "DoorDash best-cities page should expose a ranking-table jump target");
        assertTrue(bestCitiesDoc.html().contains("/doordash/where-you-can-dash"),
                "DoorDash best-cities page should bridge pay ranking to availability intent");

        MvcResult blogResult = mockMvc.perform(get("/blog/multi-apping-guide"))
                .andExpect(status().isOk())
                .andReturn();
        Document blogDoc = Jsoup.parse(blogResult.getResponse().getContentAsString(),
                AppConstants.BASE_URL + "/blog/multi-apping-guide");
        assertTrue(blogDoc.title().contains("Multi-Apping Calculator Guide"),
                "Multi-apping guide should be framed as a calculator guide");
        assertTrue(firstH1(blogDoc).contains("Multi-Apping Calculator Guide"),
                "Multi-apping guide H1 should be framed as a calculator guide");
    }

    private void assertCanonicalAndNoIndex(String path, String expectedCanonical) throws Exception {
        MvcResult result = mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andReturn();
        Document doc = Jsoup.parse(result.getResponse().getContentAsString(), AppConstants.BASE_URL + path);
        String robots = metaContent(doc, "robots");
        String canonical = canonicalHref(doc);
        assertTrue(robots != null && robots.contains("noindex"), path + " should be noindex");
        assertTrue(expectedCanonical.equals(canonical),
                path + " canonical mismatch. expected=" + expectedCanonical + ", actual=" + canonical);
    }

    private List<String> extractPageUrlsFromSitemap(String sitemap) throws Exception {
        List<String> urls = extractLocUrls(sitemap);
        if (!sitemap.contains("<sitemapindex")) {
            return urls;
        }

        List<String> pageUrls = new ArrayList<>();
        for (String sitemapUrl : urls) {
            String path = sitemapUrl.replace(AppConstants.BASE_URL, "");
            MvcResult child = mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andReturn();
            pageUrls.addAll(extractLocUrls(child.getResponse().getContentAsString()));
        }
        return pageUrls;
    }

    private static List<String> extractLocUrls(String sitemap) {
        Pattern pattern = Pattern.compile("<loc>(.*?)</loc>");
        Matcher matcher = pattern.matcher(sitemap);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            urls.add(matcher.group(1).trim());
        }
        return urls;
    }

    private static String metaContent(Document doc, String name) {
        Element meta = doc.selectFirst("meta[name=" + name + "]");
        return meta == null ? "" : meta.attr("content").trim();
    }

    private static String canonicalHref(Document doc) {
        Element canonical = doc.selectFirst("link[rel=canonical]");
        return canonical == null ? "" : canonical.attr("href").trim();
    }

    private static String firstH1(Document doc) {
        Element h1 = doc.selectFirst("h1");
        return h1 == null ? "" : h1.text().trim();
    }

    private static void writeReport(int urlCount, List<Map<String, String>> rows, List<String> warnings,
                                    List<String> failures) throws Exception {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"generatedAt\": \"").append(Instant.now()).append("\",\n");
        json.append("  \"urlCount\": ").append(urlCount).append(",\n");
        json.append("  \"warnings\": ").append(toJsonArray(warnings)).append(",\n");
        json.append("  \"failures\": ").append(toJsonArray(failures)).append(",\n");
        json.append("  \"rows\": [\n");

        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = rows.get(i);
            json.append("    {");
            int col = 0;
            for (Map.Entry<String, String> entry : row.entrySet()) {
                if (col++ > 0) {
                    json.append(", ");
                }
                json.append("\"").append(escapeJson(entry.getKey())).append("\": ");
                json.append("\"").append(escapeJson(entry.getValue())).append("\"");
            }
            json.append("}");
            if (i < rows.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}\n");

        Path output = Path.of("build", "reports", "organic-monitoring-report.json");
        Files.createDirectories(output.getParent());
        Files.writeString(output, json.toString());
    }

    private static String toJsonArray(List<String> items) {
        StringBuilder out = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                out.append(", ");
            }
            out.append("\"").append(escapeJson(items.get(i))).append("\"");
        }
        out.append("]");
        return out.toString();
    }

    private static String escapeJson(String raw) {
        return raw
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
