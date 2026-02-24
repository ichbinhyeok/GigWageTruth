package com.gigwager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlaceholderLeakTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNoPlaceholdersLeakToProduction() throws Exception {
        // Test a sample of pages to ensure no [bracketed] placeholders leak
        String[] testUrls = {
                "/salary/uber/austin",
                "/salary/doordash/houston/part-time",
                "/salary/uber/san-francisco/full-time",
                "/salary/doordash/new-york/side-hustle"
        };

        // We only want to match literal placeholder tokens like [popular nightlife
        // district]
        // But not JSON arrays or tags, so we look for [some words] that don't have HTML
        // tags inside
        Pattern placeholderPattern = Pattern.compile("\\[[^\\]]+\\]");

        for (String url : testUrls) {
            MvcResult result = mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andReturn();

            String content = result.getResponse().getContentAsString();

            // Strip JSON-LD specifically
            content = content.replaceAll("(?is)<script[^>]*type=[\"']application/ld\\+json[\"'][^>]*>.*?</script>",
                    " ");
            // Strip script blocks safely
            content = content.replaceAll("(?is)<script[^>]*>.*?</script>", " ");
            // Strip style blocks safely
            content = content.replaceAll("(?is)<style[^>]*>.*?</style>", " ");
            // Remove all remaining HTML tags
            content = content.replaceAll("(?is)<.*?>", " ");

            Matcher matcher = placeholderPattern.matcher(content);
            while (matcher.find()) {
                String match = matcher.group();
                if (!match.contains("x-cloak") && !match.contains("x-show") && !match.contains("x-text")) {
                    org.junit.jupiter.api.Assertions.fail("Found a leaked placeholder token in " + url + ": " + match);
                }
            }
        }
    }
}
