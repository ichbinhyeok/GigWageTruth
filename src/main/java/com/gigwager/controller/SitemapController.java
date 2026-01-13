package com.gigwager.controller;

import com.gigwager.model.CityData;
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

        // Main pages
        addUrl(xml, "https://www.gigwagetruth.com/", "2026-01-13", "weekly", "1.0");
        addUrl(xml, "https://www.gigwagetruth.com/uber", "2026-01-13", "weekly", "0.9");
        addUrl(xml, "https://www.gigwagetruth.com/doordash", "2026-01-13", "weekly", "0.9");

        // Salary directory
        addUrl(xml, "https://www.gigwagetruth.com/salary/directory", "2026-01-13", "weekly", "0.8");

        // Programmatic SEO: City pages (2 apps × 10 cities = 20 pages)
        for (CityData city : CityData.values()) {
            String uberUrl = "https://www.gigwagetruth.com/salary/uber/" + city.getSlug();
            String doordashUrl = "https://www.gigwagetruth.com/salary/doordash/" + city.getSlug();

            addUrl(xml, uberUrl, "2026-01-13", "monthly", "0.7");
            addUrl(xml, doordashUrl, "2026-01-13", "monthly", "0.7");
        }

        // Blog
        addUrl(xml, "https://www.gigwagetruth.com/blog", "2026-01-13", "daily", "0.8");
        addUrl(xml, "https://www.gigwagetruth.com/blog/multi-apping-guide", "2026-01-13", "monthly", "0.7");
        addUrl(xml, "https://www.gigwagetruth.com/blog/tax-guide", "2026-01-13", "monthly", "0.7");
        addUrl(xml, "https://www.gigwagetruth.com/blog/uber-vs-doordash", "2026-01-13", "monthly", "0.7");
        addUrl(xml, "https://www.gigwagetruth.com/blog/hidden-costs", "2026-01-13", "monthly", "0.7");

        // Static pages
        addUrl(xml, "https://www.gigwagetruth.com/about", "2026-01-13", "yearly", "0.5");
        addUrl(xml, "https://www.gigwagetruth.com/methodology", "2026-01-13", "yearly", "0.5");

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
