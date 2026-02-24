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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

        // Assert it is actually parsable XML
        assertDoesNotThrow(() -> {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

            // Assert root element is urlset
            assertNotNull(doc.getDocumentElement(), "Root element should not be null");
            assertEquals("urlset",
                    doc.getDocumentElement().getLocalName() != null ? doc.getDocumentElement().getLocalName()
                            : doc.getDocumentElement().getNodeName());

            NodeList urls = doc.getElementsByTagName("url");
            assertTrue(urls.getLength() >= 10, "Sitemap should have at least 10 URLs");

            // Check if required clusters are present
            assertTrue(xmlContent.contains("/taxes"), "Sitemap should contain /taxes");
            assertTrue(xmlContent.contains("/insurance"), "Sitemap should contain /insurance");
            assertTrue(xmlContent.contains("/vehicle-cost"), "Sitemap should contain /vehicle-cost");
            assertTrue(xmlContent.contains("/taxes/quarterly-estimator"),
                    "Sitemap should contain /taxes/quarterly-estimator");

        }, "Sitemap output could not be parsed as valid XML");
    }
}
