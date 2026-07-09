package com.gigwager.controller;

import com.gigwager.model.CalculatorIntentPage;
import com.gigwager.model.CityData;
import com.gigwager.model.CityIntentPage;
import com.gigwager.model.WorkLevel;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.PageIndexPolicyService;
import com.gigwager.util.AppConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

    private static final String CORE_LASTMOD = AppConstants.SITEMAP_LASTMOD_DATE;
    private static final String REPORT_LASTMOD = AppConstants.SITEMAP_LASTMOD_DATE;
    private static final String CITY_DATA_LASTMOD = "2026-07-06";
    private static final String EVERGREEN_LASTMOD = "2026-07-06";

    private final PageIndexPolicyService pageIndexPolicyService;
    private final DataLayerService dataLayerService;

    public SitemapController(PageIndexPolicyService pageIndexPolicyService, DataLayerService dataLayerService) {
        this.pageIndexPolicyService = pageIndexPolicyService;
        this.dataLayerService = dataLayerService;
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        StringBuilder xml = startUrlset();
        addCoreUrls(xml);
        addReportUrls(xml);
        addCityUrls(xml);
        addLongtailUrls(xml);
        return endUrlset(xml);
    }

    @GetMapping(value = "/sitemap-index.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemapIndex() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        addSitemap(xml, "/sitemap-core.xml", CORE_LASTMOD);
        addSitemap(xml, "/sitemap-reports.xml", REPORT_LASTMOD);
        addSitemap(xml, "/sitemap-city.xml", CITY_DATA_LASTMOD);
        addSitemap(xml, "/sitemap-longtail.xml", CITY_DATA_LASTMOD);
        xml.append("</sitemapindex>");
        return xml.toString();
    }

    @GetMapping(value = "/sitemap-core.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String coreSitemap() {
        StringBuilder xml = startUrlset();
        addCoreUrls(xml);
        return endUrlset(xml);
    }

    private void addCoreUrls(StringBuilder xml) {
        addUrl(xml, "/", CORE_LASTMOD);
        addUrl(xml, "/uber", CORE_LASTMOD);
        addUrl(xml, "/doordash", CORE_LASTMOD);
        addUrl(xml, "/salary/directory", CORE_LASTMOD);
        addUrl(xml, "/salary/uber", CORE_LASTMOD);
        addUrl(xml, "/salary/doordash", CORE_LASTMOD);
        addUrl(xml, "/best-cities/uber", CORE_LASTMOD);
        addUrl(xml, "/best-cities/doordash", CORE_LASTMOD);
        addUrl(xml, "/uber/where-you-can-drive", CORE_LASTMOD);
        addUrl(xml, "/doordash/where-you-can-dash", CORE_LASTMOD);

        for (CalculatorIntentPage intentPage : CalculatorIntentPage.values()) {
            addUrl(xml, intentPage.path(), CORE_LASTMOD);
        }

        addUrl(xml, "/uber-after-expenses", EVERGREEN_LASTMOD);
        addUrl(xml, "/doordash-after-expenses", EVERGREEN_LASTMOD);
        addUrl(xml, "/net-hourly-calculator", CORE_LASTMOD);
        addUrl(xml, "/multi-apping", EVERGREEN_LASTMOD);
        addUrl(xml, "/profit-setup-kit", EVERGREEN_LASTMOD);
        addUrl(xml, "/taxes", EVERGREEN_LASTMOD);
        addUrl(xml, "/taxes/quarterly-estimator", EVERGREEN_LASTMOD);
        addUrl(xml, "/insurance", EVERGREEN_LASTMOD);
        addUrl(xml, "/insurance/rideshare-basics", EVERGREEN_LASTMOD);
        addUrl(xml, "/vehicle-cost", EVERGREEN_LASTMOD);
        addUrl(xml, "/vehicle-cost/cost-per-mile", EVERGREEN_LASTMOD);
        addUrl(xml, "/blog", EVERGREEN_LASTMOD);
        addUrl(xml, "/blog/multi-apping-guide", EVERGREEN_LASTMOD);
        addUrl(xml, "/blog/tax-guide", EVERGREEN_LASTMOD);
        addUrl(xml, "/blog/uber-vs-doordash", EVERGREEN_LASTMOD);
        addUrl(xml, "/blog/hidden-costs", EVERGREEN_LASTMOD);
        addUrl(xml, "/methodology", EVERGREEN_LASTMOD);
    }

    @GetMapping(value = "/sitemap-reports.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String reportSitemap() {
        StringBuilder xml = startUrlset();
        addReportUrls(xml);
        return endUrlset(xml);
    }

    private void addReportUrls(StringBuilder xml) {
        addUrl(xml, "/reports/uber-driver-hourly-earnings-2026", REPORT_LASTMOD);
        addUrl(xml, "/reports/doordash-driver-hourly-pay-2026", REPORT_LASTMOD);
        addUrl(xml, "/reports/doordash-driver-shift-evidence-2026", REPORT_LASTMOD);
        addUrl(xml, "/doordash/how-much-can-you-make-in-3-hours", REPORT_LASTMOD);
        addUrl(xml, "/doordash/how-much-can-you-make-in-4-hours", REPORT_LASTMOD);
        addUrl(xml, "/doordash/how-much-can-you-make-in-6-hours", REPORT_LASTMOD);
        addUrl(xml, "/doordash/how-much-can-you-make-in-8-hours", REPORT_LASTMOD);
        addUrl(xml, "/doordash/how-much-can-you-make-in-a-day", REPORT_LASTMOD);
        addUrl(xml, "/doordash/how-much-can-you-make-in-a-week", REPORT_LASTMOD);
        addUrl(xml, "/doordash/can-you-make-100-a-day", REPORT_LASTMOD);
        addUrl(xml, "/doordash/can-you-make-200-a-day", REPORT_LASTMOD);
        addUrl(xml, "/doordash/after-gas", REPORT_LASTMOD);
        addUrl(xml, "/doordash/pay-per-mile", REPORT_LASTMOD);
    }

    @GetMapping(value = "/sitemap-city.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String citySitemap() {
        StringBuilder xml = startUrlset();
        addCityUrls(xml);
        return endUrlset(xml);
    }

    private void addCityUrls(StringBuilder xml) {
        for (String app : new String[] { "uber", "doordash" }) {
            for (CityData city : CityData.values()) {
                if (pageIndexPolicyService.isCityReportIndexable(city)) {
                    addUrl(xml, "/salary/" + app + "/" + city.getSlug(), CITY_DATA_LASTMOD);

                    if (app.equals("uber") && dataLayerService.hasRichLocalData(city.getSlug())) {
                        addUrl(xml, "/compare/" + city.getSlug() + "/uber-vs-doordash", CITY_DATA_LASTMOD);
                    }
                }
            }
        }
    }

    @GetMapping(value = "/sitemap-longtail.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String longtailSitemap() {
        StringBuilder xml = startUrlset();
        addLongtailUrls(xml);
        return endUrlset(xml);
    }

    private void addLongtailUrls(StringBuilder xml) {
        for (String app : new String[] { "uber", "doordash" }) {
            for (CityData city : CityData.values()) {
                if (pageIndexPolicyService.isCityReportIndexable(city)) {
                    for (WorkLevel workLevel : WorkLevel.values()) {
                        if (pageIndexPolicyService.isWorkLevelReportIndexable(city, workLevel)) {
                            addUrl(xml, "/salary/" + app + "/" + city.getSlug() + "/" + workLevel.getSlug(),
                                    CITY_DATA_LASTMOD);
                        }
                    }

                    for (CityIntentPage intentPage : CityIntentPage.values()) {
                        if (intentPage.isSupportedForApp(app)) {
                            addUrl(xml, "/salary/" + app + "/" + city.getSlug() + "/" + intentPage.getSlug(),
                                    CITY_DATA_LASTMOD);
                        }
                    }
                }
            }
        }
    }

    private StringBuilder startUrlset() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        return xml;
    }

    private String endUrlset(StringBuilder xml) {
        xml.append("</urlset>");
        return xml.toString();
    }

    private void addSitemap(StringBuilder xml, String path, String lastmod) {
        xml.append("    <sitemap>\n");
        xml.append("        <loc>").append(AppConstants.BASE_URL).append(path).append("</loc>\n");
        xml.append("        <lastmod>").append(lastmod).append("</lastmod>\n");
        xml.append("    </sitemap>\n");
    }

    private void addUrl(StringBuilder xml, String path, String lastmod) {
        xml.append("    <url>\n");
        xml.append("        <loc>").append(AppConstants.BASE_URL).append(path).append("</loc>\n");
        xml.append("        <lastmod>").append(lastmod).append("</lastmod>\n");
        xml.append("    </url>\n");
    }
}
