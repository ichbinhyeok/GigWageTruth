package com.gigwager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorLandingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uberLandingShouldBeIndexableWithoutQuery() throws Exception {
        MvcResult result = mockMvc.perform(get("/uber"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);
        assertTrue(content.contains("href=\"https://gigverdict.com/uber\""),
                "Uber page should self-canonicalize to /uber");
        assertTrue(content.contains("Uber weekly take-home calculator"),
                "Uber page should expose server-rendered H1 text before JavaScript runs");
        assertTrue(content.contains("aria-label=\"Share result\""),
                "Mobile sticky share action should expose an accessible name");
        assertTrue(content.contains("x-model=\"rawGross\" min=\"0\""),
                "Calculator should constrain gross pay to non-negative values");
        assertTrue(content.contains("x-model=\"rawMiles\" min=\"0\""),
                "Calculator should constrain miles to non-negative values");
        assertTrue(content.contains("x-model=\"rawHours\" min=\"0\""),
                "Calculator should constrain hours to non-negative values");
        assertEquals(1, document.select("h1").size(),
                "Uber response should render one app-specific H1");
        assertFalse(content.contains("DoorDash weekly take-home calculator"),
                "Uber response should not ship hidden DoorDash SEO copy");
        assertTrue(content.contains("href=\"/css/app.css?v="),
                "Calculator should load the compiled first-party stylesheet");
        assertFalse(content.contains("cdn.tailwindcss.com"),
                "Production HTML must not compile Tailwind in the browser");
    }

    @Test
    public void doordashLandingShouldBeIndexableWithoutQuery() throws Exception {
        MvcResult result = mockMvc.perform(get("/doordash"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);
        assertTrue(content.contains("href=\"https://gigverdict.com/doordash\""),
                "DoorDash page should self-canonicalize to /doordash");
        assertTrue(content.contains("DoorDash weekly take-home calculator"),
                "DoorDash page should expose server-rendered H1 text before JavaScript runs");
        assertTrue(content.contains("clampInput(rawGross)"),
                "DoorDash calculator should clamp negative money inputs before producing scenario links");
        assertEquals(1, document.select("h1").size(),
                "DoorDash response should render one app-specific H1");
        assertFalse(content.contains("Uber weekly take-home calculator"),
                "DoorDash response should not ship hidden Uber SEO copy");
        assertTrue(content.contains("href=\"/uber\""),
                "The app switcher should link to the other canonical calculator page");
    }
}
