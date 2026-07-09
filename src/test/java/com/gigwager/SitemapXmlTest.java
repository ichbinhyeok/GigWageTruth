package com.gigwager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.w3c.dom.NodeList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

                String xmlContent = result.getResponse().getContentAsString();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

                // Assert root element is urlset
                assertNotNull(doc.getDocumentElement(), "Root element should not be null");
                assertEquals("urlset",
                                doc.getDocumentElement().getLocalName() != null
                                                ? doc.getDocumentElement().getLocalName()
                                                : doc.getDocumentElement().getNodeName());

                NodeList urls = doc.getElementsByTagName("url");
                assertTrue(urls.getLength() >= 10, "Sitemap should have at least 10 URLs");

                // Check if required URLs are present (using correct domain)
                assertTrue(
                                xmlContent.contains("<loc>https://gigverdict.com/</loc>"),
                                "Sitemap should contain home page with correct domain");
                assertTrue(xmlContent.contains("/salary/directory"), "Sitemap should contain /salary/directory");
                assertTrue(xmlContent.contains("/reports/uber-driver-hourly-earnings-2026"),
                                "Sitemap should contain the Uber hourly earnings report");
                assertTrue(xmlContent.contains("/reports/doordash-driver-hourly-pay-2026"),
                                "Sitemap should contain the DoorDash hourly pay report");
                assertTrue(xmlContent.contains("/reports/doordash-driver-shift-evidence-2026"),
                                "Sitemap should contain the DoorDash shift evidence report");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-3-hours"),
                                "Sitemap should contain DoorDash duration earning pages");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-8-hours"),
                                "Sitemap should contain DoorDash full-day duration earning pages");
                assertTrue(xmlContent.contains("/doordash/how-much-can-you-make-in-a-week"),
                                "Sitemap should contain DoorDash weekly earning pages");
                assertTrue(xmlContent.contains("/uber"), "Sitemap should contain /uber");
                assertTrue(xmlContent.contains("/doordash"), "Sitemap should contain /doordash");
                assertTrue(xmlContent.contains("/uber/where-you-can-drive"),
                                "Sitemap should contain the Uber coverage guide");
                assertTrue(xmlContent.contains("/salary/doordash/denver/after-gas"),
                                "Sitemap should contain city intent pages");
                assertTrue(xmlContent.contains("/salary/uber/chicago/active-time"),
                                "Sitemap should contain Uber city intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/phoenix/100-a-day"),
                                "Sitemap should contain daily target intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/denver/hourly-pay"),
                                "Sitemap should contain DoorDash hourly-pay intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/phoenix/how-much-can-you-make"),
                                "Sitemap should contain DoorDash how-much-can-you-make intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/dallas/best-areas"),
                                "Sitemap should contain DoorDash best-area intent pages");
                assertTrue(xmlContent.contains("/salary/doordash/chicago/uber-eats-vs-doordash"),
                                "Sitemap should contain DoorDash app-comparison intent pages");
                org.junit.jupiter.api.Assertions.assertFalse(xmlContent.contains("/salary/uber/chicago/uber-eats-vs-doordash"),
                                "Sitemap should not contain DoorDash-only comparison intent under Uber");
                assertTrue(xmlContent.contains("/salary/uber/nashville/nights-weekends"),
                                "Sitemap should contain nights and weekends intent pages");

                assertTrue(xmlContent.contains("/taxes"), "Sitemap should contain /taxes");
                assertTrue(xmlContent.contains("/insurance"), "Sitemap should contain /insurance");
                assertTrue(xmlContent.contains("/vehicle-cost"), "Sitemap should contain /vehicle-cost");

                assertTrue(xmlContent.contains("/taxes/quarterly-estimator"),
                                "Sitemap should contain /taxes/quarterly-estimator");
                assertTrue(xmlContent.contains("/insurance/rideshare-basics"),
                                "Sitemap should contain /insurance/rideshare-basics");
                assertTrue(xmlContent.contains("/vehicle-cost/cost-per-mile"),
                                "Sitemap should contain /vehicle-cost/cost-per-mile");

                // Verify all <loc> entries use absolute https URLs and contain no query params
                java.util.regex.Pattern locPattern = java.util.regex.Pattern.compile("<loc>(.*?)</loc>");
                java.util.regex.Matcher locMatcher = locPattern.matcher(xmlContent);
                int locCount = 0;
                while (locMatcher.find()) {
                        String locUrl = locMatcher.group(1);
                        locCount++;
                        assertTrue(locUrl.startsWith("https://gigverdict.com/"),
                                        "All sitemap URLs must be absolute gigverdict.com URLs, found: " + locUrl);
                        org.junit.jupiter.api.Assertions.assertFalse(locUrl.contains("?"),
                                        "Sitemap loc must NOT contain query parameters, found: " + locUrl);
                        org.junit.jupiter.api.Assertions.assertFalse(locUrl.contains("&"),
                                        "Sitemap loc must NOT contain ampersand params, found: " + locUrl);
                }
                assertTrue(locCount >= 10, "Should have found at least 10 loc entries");

                // Verify old domain is NOT referenced
                org.junit.jupiter.api.Assertions.assertFalse(xmlContent.contains("gigwagetruth.com"),
                                "Sitemap must NOT reference old domain gigwagetruth.com");
        }
}
