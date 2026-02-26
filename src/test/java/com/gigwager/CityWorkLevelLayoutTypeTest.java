package com.gigwager;

import com.gigwager.model.content.CityRichContent;
import com.gigwager.model.content.WorkLevelRichContent;
import com.gigwager.service.CityRichContentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CityWorkLevelLayoutTypeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityRichContentRepository cityRichContentRepository;

    @Test
    public void typeBShouldRenderDayInLifeBeforeMeaning() throws Exception {
        CityRichContent city = cityRichContentRepository.findBySlug("houston")
                .orElseThrow(() -> new IllegalStateException("Missing rich content for houston"));
        WorkLevelRichContent work = city.workLevels().get("part-time");
        String dayAnchor = htmlAnchor(work.dayInTheLifeHtml());
        String meaningAnchor = htmlAnchor(work.workLevelMeaningHtml());

        MvcResult result = mockMvc.perform(get("/salary/uber/houston/part-time"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String normalizedContent = normalizeForSearch(content);
        int dayIndex = normalizedContent.indexOf(dayAnchor);
        int meaningIndex = normalizedContent.indexOf(meaningAnchor);

        assertTrue(dayIndex >= 0, "TYPE_B page should render day-in-life block");
        assertTrue(meaningIndex >= 0, "TYPE_B page should render meaning block");
        assertTrue(dayIndex < meaningIndex, "TYPE_B should place day-in-life before meaning");
    }

    @Test
    public void typeCShouldRenderTaxBeforeMeaning() throws Exception {
        CityRichContent city = cityRichContentRepository.findBySlug("san-francisco")
                .orElseThrow(() -> new IllegalStateException("Missing rich content for san-francisco"));
        WorkLevelRichContent work = city.workLevels().get("part-time");
        String taxAnchor = htmlAnchor(work.taxStrategyHtml());
        String meaningAnchor = htmlAnchor(work.workLevelMeaningHtml());

        MvcResult result = mockMvc.perform(get("/salary/uber/san-francisco/part-time"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String normalizedContent = normalizeForSearch(content);
        int taxIndex = normalizedContent.indexOf(taxAnchor);
        int meaningIndex = normalizedContent.indexOf(meaningAnchor);

        assertTrue(taxIndex >= 0, "TYPE_C page should render tax block");
        assertTrue(meaningIndex >= 0, "TYPE_C page should render meaning block");
        assertTrue(taxIndex < meaningIndex, "TYPE_C should place tax block before meaning");
    }

    @Test
    public void richContentPageShouldExposePersonaAndSources() throws Exception {
        CityRichContent city = cityRichContentRepository.findBySlug("austin")
                .orElseThrow(() -> new IllegalStateException("Missing rich content for austin"));
        WorkLevelRichContent work = city.workLevels().get("side-hustle");

        MvcResult result = mockMvc.perform(get("/salary/uber/austin/side-hustle"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Persona Validation Snapshot"),
                "Rich page should include persona section");
        assertTrue(content.contains("Sources and Methodology"),
                "Rich page should include source citation section");
        assertTrue(content.contains(city.seo().methodologyVersion()),
                "Rich page should include methodology version");
        assertTrue(content.contains(work.personaQuotes().get(0).displayName()),
                "Rich page should include persona attribution name");
    }

    private String htmlAnchor(String html) {
        String normalized = normalizeForSearch(html);
        if (normalized.length() <= 90) {
            return normalized;
        }
        return normalized.substring(0, 90);
    }

    private String normalizeForSearch(String text) {
        return text.toLowerCase(Locale.US).replaceAll("\\s+", " ").trim();
    }
}
