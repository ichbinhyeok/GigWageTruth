package com.gigwager.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                    <url>
                        <loc>https://gigwagetruth.com/</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>weekly</changefreq>
                        <priority>1.0</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/uber</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>weekly</changefreq>
                        <priority>0.9</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/doordash</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>weekly</changefreq>
                        <priority>0.9</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/blog</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>daily</changefreq>
                        <priority>0.8</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/blog/multi-apping-guide</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>monthly</changefreq>
                        <priority>0.7</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/blog/tax-guide</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>monthly</changefreq>
                        <priority>0.7</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/blog/uber-vs-doordash</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>monthly</changefreq>
                        <priority>0.7</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/about</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>yearly</changefreq>
                        <priority>0.5</priority>
                    </url>
                    <url>
                        <loc>https://gigwagetruth.com/methodology</loc>
                        <lastmod>2026-01-12</lastmod>
                        <changefreq>yearly</changefreq>
                        <priority>0.5</priority>
                    </url>
                </urlset>
                """;
    }
}
