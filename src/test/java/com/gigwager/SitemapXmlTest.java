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
                assertTrue(xmlContent.contains("/uber"), "Sitemap should contain /uber");
                assertTrue(xmlContent.contains("/doordash"), "Sitemap should contain /doordash");

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
