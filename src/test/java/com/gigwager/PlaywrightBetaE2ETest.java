package com.gigwager;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlaywrightBetaE2ETest {

    @LocalServerPort
    private int localPort;

    private String baseUrl;
    private Playwright playwright;
    private Browser browser;

    @BeforeAll
    public void setUp() {
        String overrideBaseUrl = System.getProperty("e2e.baseUrl");
        baseUrl = overrideBaseUrl != null && !overrideBaseUrl.isBlank()
                ? overrideBaseUrl
                : "http://localhost:" + localPort;
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void rideshareUserCanSubmitFromHomeAndReachUberCalculator() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/");
            page.locator("input[name='gross']").fill("1200");
            page.locator("input[name='miles']").fill("780");
            page.locator("input[name='hours']").fill("38");
            page.locator("button:has-text('Calculate My Truth')").click();

            page.waitForURL(url -> url.contains("/uber"));
            assertTrue(page.url().contains("/uber"), "Expected to land on /uber calculator");
            String title = page.title() == null ? "" : page.title().toLowerCase();
            assertTrue(title.contains("truth") || title.contains("uber"),
                    "Expected Uber calculator title");
        }
    }

    @Test
    public void deliveryUserCanSwitchTabAndReachDoorDashCalculator() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/");
            page.locator("button:has-text('DoorDash')").click();
            page.locator("input[name='gross']").fill("900");
            page.locator("input[name='miles']").fill("620");
            page.locator("input[name='hours']").fill("32");
            page.locator("button:has-text('Calculate My Truth')").click();

            page.waitForURL(url -> url.contains("/doordash"));
            assertTrue(page.url().contains("/doordash"), "Expected to land on /doordash calculator");
        }
    }

    @Test
    public void calculatorShouldReactToInputChanges() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/uber?gross=1100&miles=700&hours=35");
            page.waitForLoadState();

            String before = page.locator("span[x-text='netHourly.toFixed(2)']").first().innerText().trim();
            page.locator("input[x-model='rawMiles']").first().fill("1200");
            page.waitForTimeout(700);
            String after = page.locator("span[x-text='netHourly.toFixed(2)']").first().innerText().trim();

            assertNotEquals(before, after, "Net hourly should update after input change");
        }
    }

    @Test
    public void cityAndWorkLevelPagesShouldShowTrustAndFaqSignals() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/salary/uber/san-francisco");
            assertTrue(page.locator("h1").first().innerText().contains("San Francisco"),
                    "City report must show city-specific heading");
            assertTrue(page.getByText("Frequently Asked Questions").first().isVisible(),
                    "City report should include visible FAQ");

            page.navigate(baseUrl + "/salary/uber/new-york/full-time");
            assertTrue(page.getByText("Source-backed page").first().isVisible(),
                    "Work-level page should expose source-backed trust signal near top");
            assertTrue(page.getByText("Sources and Methodology").first().isVisible(),
                    "Work-level page should show source section");
            assertTrue(page.getByText("Frequently Asked Questions").first().isVisible(),
                    "Work-level page should include visible FAQ");
        }
    }

    @Test
    public void nonIndexablePagesShouldExposeNoindexAndParentCanonical() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/salary/uber/jacksonville/part-time");
            String robots = page.locator("meta[name='robots']").first().getAttribute("content");
            String canonical = page.locator("link[rel='canonical']").first().getAttribute("href");

            assertEquals("noindex,follow", robots);
            assertEquals("https://gigverdict.com/salary/uber/jacksonville", canonical);
        }
    }

    @Test
    public void mobileUserJourneyShouldRemainUsable() {
        try (BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(412, 915)
                .setDeviceScaleFactor(2.625)
                .setUserAgent("Mozilla/5.0 (Linux; Android 14; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Mobile Safari/537.36")
                .setIsMobile(true)
                .setHasTouch(true));
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/");
            page.locator("input[name='gross']").fill("1000");
            page.locator("input[name='miles']").fill("700");
            page.locator("input[name='hours']").fill("40");
            page.locator("button:has-text('Calculate My Truth')").click();
            page.waitForURL(url -> url.contains("/uber"));
            assertTrue(page.url().contains("/uber"), "Mobile flow should route to calculator page");
        }
    }

    @Test
    public void calculatorShouldShowActionCtasAndFixedAssumptions() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/uber?gross=1050&miles=740&hours=40");
            assertTrue(page.getByText("Recommended Next Step").first().isVisible(),
                    "Result area should expose recommended next action");
            assertTrue(page.getByText("Standard Assumptions").first().isVisible(),
                    "Result area should expose fixed assumptions");
            assertTrue(page.getByText("IRS mileage proxy: $0.725/mi (2026).").first().isVisible(),
                    "Assumption block should include IRS mileage baseline");
            assertTrue(page.getByText("Self-employment tax estimate: 15.3%.").first().isVisible(),
                    "Assumption block should include SE tax baseline");
        }
    }

    @Test
    public void recommendationShouldAdaptForLowNetScenario() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/uber?gross=500&miles=1200&hours=45");
            assertTrue(page.getByText("Fix cost per mile first").first().isVisible(),
                    "Low-net scenario should prioritize cost-per-mile action");
            String href = page.locator("a:has-text('Fix cost per mile first')").first().getAttribute("href");
            assertTrue(href != null && href.contains("/vehicle-cost/cost-per-mile"),
                    "Low-net recommendation should route to cost-per-mile tool");
        }
    }

    @Test
    public void mobileShouldUseCompactSummaryForLongSections() {
        try (BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(390, 844)
                .setDeviceScaleFactor(3)
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 17_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Mobile/15E148 Safari/604.1")
                .setIsMobile(true)
                .setHasTouch(true));
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/uber?gross=1000&miles=700&hours=40");
            page.getByText("Quick assumptions (tap for full method)").first().scrollIntoViewIfNeeded();
            assertTrue(page.getByText("Quick assumptions (tap for full method)").first().isVisible(),
                    "Mobile should show collapsible assumptions summary");
            assertTrue(page.getByText("1) Set your quarterly tax plan →").first().isVisible(),
                    "Mobile should show compact next-action links");
        }
    }

    @Test
    public void clusterIntentLinksShouldBeReachableFromWorkLevelPage() {
        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            page.navigate(baseUrl + "/salary/uber/san-francisco/full-time");

            assertTrue(page.locator("a[href='/taxes/quarterly-estimator']").count() > 0,
                    "Work-level page should link to quarterly tax estimator");
            assertTrue(page.locator("a[href='/insurance/rideshare-basics']").count() > 0,
                    "Work-level page should link to rideshare insurance basics");
            assertTrue(page.locator("a[href='/vehicle-cost/cost-per-mile']").count() > 0,
                    "Work-level page should link to cost-per-mile tool");

            page.locator("a[href='/taxes/quarterly-estimator']").first().click();
            page.waitForURL(url -> url.contains("/taxes/quarterly-estimator"));
            assertTrue(page.locator("h1").first().innerText().toLowerCase().contains("quarterly"),
                    "Quarterly estimator landing should open from cluster CTA");
        }
    }

    @Test
    public void blogPagesShouldNotShowEncodingCorruptionTokens() {
        List<String> blogPaths = List.of(
                "/blog",
                "/blog/hidden-costs",
                "/blog/uber-vs-doordash",
                "/blog/multi-apping-guide",
                "/blog/tax-guide");

        try (BrowserContext context = browser.newContext();
                Page page = context.newPage()) {
            for (String path : blogPaths) {
                page.navigate(baseUrl + path);
                String bodyText = page.locator("body").innerText();
                assertTrue(!bodyText.contains("�"), path + " should not include replacement character");
                assertTrue(!bodyText.contains("?뫛"), path + " should not include mojibake token");
                assertTrue(!bodyText.contains("?썳"), path + " should not include mojibake token");
            }
        }
    }

    @Test
    public void ecosystemCrawlShouldGenerateBetaReportForTopLandingPages() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/sitemap.xml")).GET().build();
        HttpResponse<String> sitemapResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<String> urls = extractLocUrls(sitemapResponse.body());
        List<String> top20 = urls.stream().limit(20).toList();

        List<String> failures = new ArrayList<>();
        List<Map<String, String>> rows = new ArrayList<>();

        try (BrowserContext context = browser.newContext()) {
            for (String url : top20) {
                try (Page page = context.newPage()) {
                    injectVitalsProbe(page);
                    page.navigate(url);
                    page.waitForLoadState();
                    page.waitForTimeout(1000);

                    String title = page.title() == null ? "" : page.title().trim();
                    String canonical = attrOrEmpty(page, "link[rel='canonical']", "href");
                    String h1 = textOrEmpty(page, "h1");
                    String robots = attrOrEmpty(page, "meta[name='robots']", "content");
                    String cls = String.valueOf(page.evaluate("() => (window.__betaVitals && window.__betaVitals.cls) || 0"));
                    String lcp = String.valueOf(page.evaluate("() => (window.__betaVitals && window.__betaVitals.lcp) || 0"));
                    String scriptCount = String.valueOf(page.evaluate(
                            "() => performance.getEntriesByType('resource').filter(e => e.initiatorType === 'script').length"));

                    if (title.isBlank()) {
                        failures.add(url + " missing title");
                    }
                    if (canonical.isBlank()) {
                        failures.add(url + " missing canonical");
                    }
                    if (h1.isBlank()) {
                        failures.add(url + " missing h1");
                    }

                    double clsValue = safeDouble(cls);
                    double lcpValue = safeDouble(lcp);
                    if (clsValue > 0.35) {
                        failures.add(url + " CLS too high: " + clsValue);
                    }
                    if (lcpValue > 10000) {
                        failures.add(url + " LCP too high(ms): " + lcpValue);
                    }

                    Map<String, String> row = new LinkedHashMap<>();
                    row.put("url", url);
                    row.put("titleLength", String.valueOf(title.length()));
                    row.put("hasCanonical", String.valueOf(!canonical.isBlank()));
                    row.put("hasH1", String.valueOf(!h1.isBlank()));
                    row.put("robots", robots);
                    row.put("cls", cls);
                    row.put("lcpMs", lcp);
                    row.put("scriptResourceCount", scriptCount);
                    rows.add(row);
                }
            }
        }

        writeBetaReport(rows, failures);
        assertTrue(failures.isEmpty(), "Playwright ecosystem failures:\n - " + String.join("\n - ", failures));
    }

    private static void injectVitalsProbe(Page page) {
        page.addInitScript(
                "(() => {" +
                        "window.__betaVitals = { cls: 0, lcp: 0 };" +
                        "try {" +
                        "let cls = 0;" +
                        "new PerformanceObserver((list) => {" +
                        "for (const entry of list.getEntries()) {" +
                        "if (!entry.hadRecentInput) cls += entry.value;" +
                        "}" +
                        "window.__betaVitals.cls = cls;" +
                        "}).observe({ type: 'layout-shift', buffered: true });" +
                        "} catch (e) {}" +
                        "try {" +
                        "new PerformanceObserver((list) => {" +
                        "const entries = list.getEntries();" +
                        "const last = entries[entries.length - 1];" +
                        "if (last) window.__betaVitals.lcp = last.startTime;" +
                        "}).observe({ type: 'largest-contentful-paint', buffered: true });" +
                        "} catch (e) {}" +
                        "})();");
    }

    private List<String> extractLocUrls(String xml) {
        Pattern pattern = Pattern.compile("<loc>(.*?)</loc>");
        Matcher matcher = pattern.matcher(xml);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            urls.add(matcher.group(1).trim().replace("https://gigverdict.com", baseUrl));
        }
        return urls;
    }

    private static String attrOrEmpty(Page page, String selector, String attr) {
        if (page.locator(selector).count() == 0) {
            return "";
        }
        String value = page.locator(selector).first().getAttribute(attr);
        return value == null ? "" : value.trim();
    }

    private static String textOrEmpty(Page page, String selector) {
        if (page.locator(selector).count() == 0) {
            return "";
        }
        String value = page.locator(selector).first().innerText();
        return value == null ? "" : value.trim();
    }

    private static double safeDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static void writeBetaReport(List<Map<String, String>> rows, List<String> failures) throws Exception {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"generatedAt\": \"").append(Instant.now()).append("\",\n");
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
        json.append("  ],\n");
        json.append("  \"failures\": ").append(toJsonArray(failures)).append("\n");
        json.append("}\n");

        Path output = Path.of("build", "reports", "playwright-beta-report.json");
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
