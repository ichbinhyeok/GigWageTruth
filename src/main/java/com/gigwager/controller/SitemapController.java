package com.gigwager.controller;

import com.gigwager.model.CityData;
import com.gigwager.model.WorkLevel;
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
        addUrl(xml, AppConstants.BASE_URL + "/uber", today, "weekly", "0.9");
        addUrl(xml, AppConstants.BASE_URL + "/doordash", today, "weekly", "0.9");

        // Salary directory (Hub page - highest priority)
        addUrl(xml, AppConstants.BASE_URL + "/salary/directory", today, "weekly", "0.9");

        // Programmatic SEO: Main City pages (2 apps × 50 cities = 100 pages)
        for (CityData city : CityData.values()) {
            String uberUrl = AppConstants.BASE_URL + "/salary/uber/" + city.getSlug();
            String doordashUrl = AppConstants.BASE_URL + "/salary/doordash/" + city.getSlug();

            addUrl(xml, uberUrl, today, "monthly", "0.8");
            addUrl(xml, doordashUrl, today, "monthly", "0.8");
        }

        // Programmatic SEO: Work-Level Deep-Dives (2 apps × 50 cities × 3 levels = 300
        // pages)
        for (CityData city : CityData.values()) {
            for (WorkLevel level : WorkLevel.values()) {
                String uberWorkUrl = AppConstants.BASE_URL + "/salary/uber/" + city.getSlug() + "/" + level.getSlug();
                String doordashWorkUrl = AppConstants.BASE_URL + "/salary/doordash/" + city.getSlug() + "/"
                        + level.getSlug();

                addUrl(xml, uberWorkUrl, today, "monthly", "0.6");
                addUrl(xml, doordashWorkUrl, today, "monthly", "0.6");
            }
        }

        // Blog
        addUrl(xml, AppConstants.BASE_URL + "/blog", today, "daily", "0.8");
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
