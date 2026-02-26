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
public class CalculatorLandingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void uberLandingShouldBeIndexableWithoutQuery() throws Exception {
        MvcResult result = mockMvc.perform(get("/uber"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("href=\"https://gigverdict.com/uber\""),
                "Uber page should self-canonicalize to /uber");
    }

    @Test
    public void doordashLandingShouldBeIndexableWithoutQuery() throws Exception {
        MvcResult result = mockMvc.perform(get("/doordash"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("href=\"https://gigverdict.com/doordash\""),
                "DoorDash page should self-canonicalize to /doordash");
    }
}

