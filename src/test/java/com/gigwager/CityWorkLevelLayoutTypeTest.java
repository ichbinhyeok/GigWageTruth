package com.gigwager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CityWorkLevelLayoutTypeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void typeBShouldRenderDayInLifeBeforeMeaning() throws Exception {
        MvcResult result = mockMvc.perform(get("/salary/uber/houston/part-time"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int dayIndex = content.indexOf("Low-friction part-time sequence");
        int meaningIndex = content.indexOf("Part-time in Houston depends on zone choice");

        assertTrue(dayIndex >= 0, "TYPE_B page should render day-in-life block");
        assertTrue(meaningIndex >= 0, "TYPE_B page should render meaning block");
        assertTrue(dayIndex < meaningIndex, "TYPE_B should place day-in-life before meaning");
    }

    @Test
    public void typeCShouldRenderTaxBeforeMeaning() throws Exception {
        MvcResult result = mockMvc.perform(get("/salary/uber/san-francisco/part-time"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        int taxIndex = content.indexOf("Tax hygiene for high-cost markets");
        int meaningIndex = content.indexOf("Part-time in SF rewards precision");

        assertTrue(taxIndex >= 0, "TYPE_C page should render tax block");
        assertTrue(meaningIndex >= 0, "TYPE_C page should render meaning block");
        assertTrue(taxIndex < meaningIndex, "TYPE_C should place tax block before meaning");
    }

    @Test
    public void richContentPageShouldExposePersonaAndSources() throws Exception {
        MvcResult result = mockMvc.perform(get("/salary/uber/austin/side-hustle"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Persona Validation Snapshot"),
                "Rich page should include persona section");
        assertTrue(content.contains("Sources and Methodology"),
                "Rich page should include source citation section");
        assertTrue(content.contains("city-rich-v2.1"),
                "Rich page should include methodology version");
    }
}
