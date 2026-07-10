package com.gigwager;

import com.gigwager.util.AppConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RenderedSeoUniquenessTest {

    private static final Path COMPILED_CSS = Path.of("src/main/resources/static/css/app.css");

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homeCanonicalAndTaxTitlesShouldNotCompeteWithSiblingPages() throws Exception {
        Document home = render("/");
        Document taxHub = render("/taxes");
        Document taxGuide = render("/blog/tax-guide");

        assertEquals(AppConstants.BASE_URL + "/", home.selectFirst("link[rel=canonical]").attr("href"),
                "Home canonical should exactly match the root sitemap URL");
        assertNotEquals(taxHub.title(), taxGuide.title(),
                "Tax hub and editorial guide need distinct result titles");
    }

    @Test
    public void cityIntentPagesShouldExposeCitedLocalOperatingContext() throws Exception {
        Document houston = render("/salary/uber/houston/after-gas");
        Document dallas = render("/salary/uber/dallas/after-gas");
        Document houstonDoorDash = render("/salary/doordash/houston/after-gas");
        Document sanAntonioDoorDash = render("/salary/doordash/san-antonio/after-gas");

        assertTrue(houston.text().contains("Local operating context"));
        assertTrue(houston.text().contains("Local sources checked"));
        assertTrue(dallas.text().contains("Local operating context"));

        double similarity = jaccard(
                shingles(normalize(houston.select("main").text(), "houston"), 5),
                shingles(normalize(dallas.select("main").text(), "dallas"), 5));
        assertTrue(similarity < 0.92,
                "Rendered city-intent pages are still too template-like after normalization: " + similarity);

        double doorDashSimilarity = jaccard(
                shingles(normalize(houstonDoorDash.select("main").text(), "houston"), 5),
                shingles(normalize(sanAntonioDoorDash.select("main").text(), "san antonio"), 5));
        assertTrue(doorDashSimilarity < 0.92,
                "Rendered DoorDash city-intent pages are still too template-like: " + doorDashSimilarity);
    }

    @Test
    public void productionStylesheetShouldBeCompiledAndSubstantial() throws Exception {
        assertTrue(Files.exists(COMPILED_CSS), "Compiled Tailwind CSS must be committed for production");
        assertTrue(Files.size(COMPILED_CSS) > 40_000,
                "Compiled stylesheet looks incomplete; regenerate it with npm run build:css");
    }

    @Test
    public void cityReportsShouldCarryDistinctLocalEvidence() throws Exception {
        Document dallas = render("/salary/uber/dallas");
        Document sanAntonio = render("/salary/uber/san-antonio");

        assertTrue(dallas.text().contains("City operating evidence"));
        assertTrue(sanAntonio.text().contains("City operating evidence"));

        double similarity = jaccard(
                shingles(normalize(dallas.select("main").text(), "dallas"), 5),
                shingles(normalize(sanAntonio.select("main").text(), "san antonio"), 5));
        assertTrue(similarity < 0.92,
                "Rendered city reports are still too template-like: " + similarity);
    }

    private Document render(String path) throws Exception {
        String html = mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return Jsoup.parse(html, AppConstants.BASE_URL + path);
    }

    private String normalize(String text, String city) {
        return text.toLowerCase(Locale.US)
                .replace(city, "city")
                .replaceAll("\\d+(?:\\.\\d+)?", " # ")
                .replaceAll("[^a-z#]+", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private Set<String> shingles(String text, int size) {
        String[] words = text.split(" ");
        Set<String> shingles = new HashSet<>();
        for (int i = 0; i <= words.length - size; i++) {
            shingles.add(String.join(" ", java.util.Arrays.copyOfRange(words, i, i + size)));
        }
        return shingles;
    }

    private double jaccard(Set<String> left, Set<String> right) {
        Set<String> intersection = new HashSet<>(left);
        intersection.retainAll(right);
        Set<String> union = new HashSet<>(left);
        union.addAll(right);
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
}
