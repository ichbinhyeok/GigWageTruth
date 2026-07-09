package com.gigwager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SitemapXmlTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testSitemapIsParseableXml() throws Exception {
                MvcResult result = mockMvc.perform(get("/sitemap.xml"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith("application/xml"))
                                .andReturn();

                String rootSitemap = result.getResponse().getContentAsString();
                org.w3c.dom.Document rootDoc = parseXml(rootSitemap);

                assertNotNull(rootDoc.getDocumentElement(), "Root element should not be null");
                assertEquals("urlset", localName(rootDoc));

                NodeList rootUrls = rootDoc.getElementsByTagName("url");
                assertTrue(rootUrls.getLength() >= 10, "Root sitemap should expose page URLs directly for GSC");

                String sitemapIndex = readPath("/sitemap-index.xml");
                org.w3c.dom.Document indexDoc = parseXml(sitemapIndex);

                assertNotNull(indexDoc.getDocumentElement(), "Root element should not be null");
                assertEquals("sitemapindex", localName(indexDoc));

                NodeList sitemapNodes = indexDoc.getElementsByTagName("sitemap");
                assertTrue(sitemapNodes.getLength() >= 4, "Sitemap index should include staged child sitemaps");
                assertTrue(sitemapIndex.contains("/sitemap-core.xml"), "Sitemap index should contain core sitemap");
                assertTrue(sitemapIndex.contains("/sitemap-reports.xml"), "Sitemap index should contain reports sitemap");
                assertTrue(sitemapIndex.contains("/sitemap-city.xml"), "Sitemap index should contain city sitemap");
                assertTrue(sitemapIndex.contains("/sitemap-longtail.xml"), "Sitemap index should contain longtail sitemap");

                String xmlContent = rootSitemap + "\n" + readChildSitemaps(sitemapIndex);
                org.w3c.dom.Document coreDoc = parseXml(readPath("/sitemap-core.xml"));
                assertEquals("urlset", localName(coreDoc));

                NodeList urls = coreDoc.getElementsByTagName("url");
                assertTrue(urls.getLength() >= 10, "Core sitemap should have at least 10 URLs");

                assertTrue(xmlContent.contains("<loc>https://gigverdict.com/</loc>"),
                                "Sitemaps should contain home page with correct domain");
                assertTrue(xmlContent.contains("/salary/directory"), "Sitemaps should contain /salary/directory");
                assertTrue(xmlContent.contains("/doordash/earnings-calculator"),
                                "Sitemaps should contain DoorDash earnings calculator");
                assertTrue(xmlContent.contains("/doordash/gas-calculator"),
                                "Sitemaps should contain DoorDash gas calculator");
                assertTrue(xmlContent.contains("/doordash/mileage-deduction-calculator"),
                                "Sitemaps should contain DoorDash mileage deduction calculator");
                assertTrue(xmlContent.contains("/uber/pay-calculator"),
                                "Sitemaps should contain Uber pay calculator");
                assertTrue(xmlContent.contains("/uber/income-calculator"),
                                "Sitemaps should contain Uber income calculator");
                assertTrue(xmlContent.contains("/uber/tlc-pay-calculator"),
                                "Sitemaps should contain TLC pay calculator");
                assertTrue(xmlContent.contains("/reports/uber-driver-hourly-earnings-2026"),
                                "Sitemaps should contain the Uber hourly earnings report");
                assertTrue(xmlContent.contains("/reports/doordash-driver-hourly-pay-2026"),
                                "Sitemaps should contain the DoorDash hourly pay report");
                assertTrue(xmlContent.contains("/reports/doordash-driver-shift-evidence-2026"),
                                "Sitemaps should contain the DoorDash shift evidence report");
                assertTrue(xmlContent.contains("/doordash/adjustment-pay-calculator"),
                                "Sitemaps should contain the DoorDash adjustment pay calculator");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-3-hours"),
                                "Sitemaps should contain DoorDash duration earning pages");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-8-hours"),
                                "Sitemaps should contain DoorDash full-day duration earning pages");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-a-day"),
                                "Sitemaps should contain DoorDash daily earning pages");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-a-week"),
                                "Sitemaps should contain DoorDash weekly earning pages");
                assertTrue(xmlContent.contains("/doordash/can-you-make-100-a-day"),
                                "Sitemaps should contain DoorDash $100/day money-intent pages");
                assertTrue(xmlContent.contains("/doordash/can-you-make-200-a-day"),
                                "Sitemaps should contain DoorDash $200/day money-intent pages");
                assertTrue(xmlContent.contains("/doordash/after-gas"),
                                "Sitemaps should contain DoorDash after-gas money-intent pages");
                assertTrue(xmlContent.contains("/doordash/pay-per-mile"),
                                "Sitemaps should contain DoorDash pay-per-mile money-intent pages");
                assertTrue(xmlContent.contains("/uber"), "Sitemaps should contain /uber");
                assertTrue(xmlContent.contains("/doordash"), "Sitemaps should contain /doordash");
                assertTrue(xmlContent.contains("/uber/where-you-can-drive"),
                                "Sitemaps should contain the Uber coverage guide");
                assertTrue(xmlContent.contains("/salary/doordash/denver/after-gas"),
                                "Sitemaps should contain city intent pages");
                assertTrue(xmlContent.contains("/salary/uber/chicago/active-time"),
                                "Sitemaps should contain Uber city intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/phoenix/100-a-day"),
                                "Sitemaps should contain daily target intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/denver/hourly-pay"),
                                "Sitemaps should contain DoorDash hourly-pay intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/phoenix/how-much-can-you-make"),
                                "Sitemaps should contain DoorDash how-much-can-you-make intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/dallas/best-areas"),
                                "Sitemaps should contain DoorDash best-area intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/chicago/uber-eats-vs-doordash"),
                                "Sitemaps should contain DoorDash app-comparison intent pages");
                assertFalse(xmlContent.contains("/salary/uber/chicago/uber-eats-vs-doordash"),
                                "Sitemaps should not contain DoorDash-only comparison intent under Uber");
                assertTrue(xmlContent.contains("/salary/uber/nashville/nights-weekends"),
                                "Sitemaps should contain nights and weekends intent pages");
                assertFalse(xmlContent.contains("/salary/uber/orlando/after-gas"),
                                "Sitemaps should not contain non-rich city intent pages");
                assertFalse(xmlContent.contains("/salary/doordash/denver/1000-a-month"),
                                "Sitemaps should not contain lower-priority monthly target permutations");
                assertFalse(xmlContent.contains("/salary/doordash/denver/worth-it"),
                                "Sitemaps should not contain broad worth-it permutations");
                assertTrue(xmlContent.contains("/taxes"), "Sitemaps should contain /taxes");
                assertTrue(xmlContent.contains("/insurance"), "Sitemaps should contain /insurance");
                assertTrue(xmlContent.contains("/vehicle-cost"), "Sitemaps should contain /vehicle-cost");
                assertTrue(xmlContent.contains("/taxes/quarterly-estimator"),
                                "Sitemaps should contain /taxes/quarterly-estimator");
                assertTrue(xmlContent.contains("/insurance/rideshare-basics"),
                                "Sitemaps should contain /insurance/rideshare-basics");
                assertTrue(xmlContent.contains("/vehicle-cost/cost-per-mile"),
                                "Sitemaps should contain /vehicle-cost/cost-per-mile");

                List<String> locUrls = extractLocUrls(xmlContent);
                for (String locUrl : locUrls) {
                        assertTrue(locUrl.startsWith("https://gigverdict.com/"),
                                        "All sitemap URLs must be absolute gigverdict.com URLs, found: " + locUrl);
                        assertFalse(locUrl.contains("?"), "Sitemap loc must NOT contain query parameters, found: " + locUrl);
                        assertFalse(locUrl.contains("&"), "Sitemap loc must NOT contain ampersand params, found: " + locUrl);
                }
                assertTrue(locUrls.size() >= 10, "Should have found at least 10 loc entries");
                List<String> rootLocUrls = extractLocUrls(rootSitemap);
                assertTrue(rootLocUrls.size() < 650,
                                "Root sitemap should stay focused on evidence-backed pSEO pages, found: "
                                                + rootLocUrls.size());
                assertFalse(xmlContent.contains("gigwagetruth.com"),
                                "Sitemap must NOT reference old domain gigwagetruth.com");
                assertFalse(xmlContent.contains("<priority>"), "Sitemaps should not emit ignored priority hints");
                assertFalse(xmlContent.contains("<changefreq>"), "Sitemaps should not emit ignored changefreq hints");
        }

        private String readChildSitemaps(String sitemapIndex) throws Exception {
                StringBuilder combined = new StringBuilder();
                for (String loc : extractLocUrls(sitemapIndex)) {
                        String path = loc.replace("https://gigverdict.com", "");
                        combined.append(readPath(path)).append("\n");
                }
                return combined.toString();
        }

        private String readPath(String path) throws Exception {
                return mockMvc.perform(get(path))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith("application/xml"))
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
        }

        private org.w3c.dom.Document parseXml(String xmlContent) throws Exception {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                return builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));
        }

        private String localName(org.w3c.dom.Document doc) {
                return doc.getDocumentElement().getLocalName() != null
                                ? doc.getDocumentElement().getLocalName()
                                : doc.getDocumentElement().getNodeName();
        }

        private List<String> extractLocUrls(String xmlContent) {
                Pattern locPattern = Pattern.compile("<loc>(.*?)</loc>");
                Matcher locMatcher = locPattern.matcher(xmlContent);
                List<String> locUrls = new ArrayList<>();
                while (locMatcher.find()) {
                        locUrls.add(locMatcher.group(1));
                }
                return locUrls;
        }
}
