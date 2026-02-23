package com.gigwager.controller;

import com.gigwager.util.AppConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Freshness Signal: Set lastmod to the 1st day of the current month
        // This creates a "Monthly Report" trust signal rather than a "Daily Spam"
        // signal
        String today = java.time.YearMonth.now().atDay(1).toString();

        // Main pages
        addUrl(xml, AppConstants.BASE_URL + "/", today, "weekly", "1.0");

        // Salary directory (Hub page - highest priority)
        addUrl(xml, AppConstants.BASE_URL + "/salary/directory", today, "weekly", "0.9");

        // Temporary: Programmatic SEO pages (City & Work-Level) are excluded
        // from the sitemap here. They will be added back in PR5 ONLY IF they
        // pass the new PageIndexPolicyService (Quality Gate) to prevent mass
        // thin-content penalties.

        // Blog
        addUrl(xml, AppConstants.BASE_URL + "/blog", today, "monthly", "0.8");
        addUrl(xml, AppConstants.BASE_URL + "/blog/multi-apping-guide", today, "monthly", "0.7");
        addUrl(xml, AppConstants.BASE_URL + "/blog/tax-guide", today, "monthly", "0.7");
        addUrl(xml, AppConstants.BASE_URL + "/blog/uber-vs-doordash", today, "monthly", "0.7");
        addUrl(xml, AppConstants.BASE_URL + "/blog/hidden-costs", today, "monthly", "0.7");

        // Static pages
        addUrl(xml, AppConstants.BASE_URL + "/about", today, "yearly", "0.5");
        addUrl(xml, AppConstants.BASE_URL + "/methodology", today, "yearly", "0.5");

        xml.append("</urlset>");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String loc, String lastmod, String changefreq, String priority) {
        xml.append("    <url>\n");
        xml.append("        <loc>").append(loc).append("</loc>\n");
        xml.append("        <lastmod>").append(lastmod).append("</lastmod>\n");
        xml.append("        <changefreq>").append(changefreq).append("</changefreq>\n");
        xml.append("        <priority>").append(priority).append("</priority>\n");
        xml.append("    </url>\n");
    }
}
