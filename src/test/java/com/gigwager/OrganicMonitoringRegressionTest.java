package com.gigwager;

import com.gigwager.util.AppConstants;
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

        List<String> topUrls = extractLocUrls(sitemap).stream()
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
