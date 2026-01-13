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

        String today = java.time.LocalDate.now().toString();

        // Main pages
        addUrl(xml, "https://www.gigwagetruth.com/", today, "weekly", "1.0");
        addUrl(xml, "https://www.gigwagetruth.com/uber", today, "weekly", "0.9");
        addUrl(xml, "https://www.gigwagetruth.com/doordash", today, "weekly", "0.9");

        // Salary directory
        addUrl(xml, "https://www.gigwagetruth.com/salary/directory", today, "weekly", "0.8");

        // Programmatic SEO: City pages (2 apps × 10 cities = 20 pages)
        for (CityData city : CityData.values()) {
            String uberUrl = "https://www.gigwagetruth.com/salary/uber/" + city.getSlug();
            String doordashUrl = "https://www.gigwagetruth.com/salary/doordash/" + city.getSlug();

            addUrl(xml, uberUrl, today, "monthly", "0.7");
            addUrl(xml, doordashUrl, today, "monthly", "0.7");
        }

        // Blog
        addUrl(xml, "https://www.gigwagetruth.com/blog", today, "daily", "0.8");
        addUrl(xml, "https://www.gigwagetruth.com/blog/multi-apping-guide", today, "monthly", "0.7");
        addUrl(xml, "https://www.gigwagetruth.com/blog/tax-guide", today, "monthly", "0.7");
        addUrl(xml, "https://www.gigwagetruth.com/blog/uber-vs-doordash", today, "monthly", "0.7");
        addUrl(xml, "https://www.gigwagetruth.com/blog/hidden-costs", today, "monthly", "0.7");

        // Static pages
        addUrl(xml, "https://www.gigwagetruth.com/about", today, "yearly", "0.5");
        addUrl(xml, "https://www.gigwagetruth.com/methodology", today, "yearly", "0.5");

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
