package com.gigwager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CityRichContentDuplicationGuardTest {

    private static final Path CITY_DIR = Path.of("src/main/resources/data/cities");
    private static final int NGRAM_SIZE = 3;
    // Guardrail for cross-city differentiator content.
    private static final double MAX_DIFFERENTIATOR_SIMILARITY = 0.52;
    // Field-level caps to prevent template skeleton drift.
    private static final Map<String, Double> FIELD_SIMILARITY_CAPS = Map.of(
            "workLevelMeaningHtml", 0.67,
            "dayInTheLifeHtml", 0.55,
            "taxStrategyHtml", 0.68,
            "bestPracticesHtml", 0.78);
    private static final Pattern TAGS = Pattern.compile("<[^>]+>");
    private static final Pattern NON_ALNUM = Pattern.compile("[^a-z0-9\\s]");
    private static final Pattern MULTI_SPACE = Pattern.compile("\\s+");

    @Test
    public void cityDifferentiatorContentShouldNotBeNearDuplicate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Path> files = Files.list(CITY_DIR)
                .filter(p -> p.toString().endsWith(".json"))
                .toList();

        Map<String, Set<String>> cityNgrams = new HashMap<>();
        for (Path file : files) {
            JsonNode root = mapper.readTree(file.toFile());
            String slug = root.path("citySlug").asText();
            String corpus = buildDifferentiatorCorpus(root);
            cityNgrams.put(slug, toNgrams(corpus, NGRAM_SIZE));
        }

        List<SimilarityRow> rows = new ArrayList<>();
        List<String> slugs = cityNgrams.keySet().stream().sorted().toList();
        for (int i = 0; i < slugs.size(); i++) {
            for (int j = i + 1; j < slugs.size(); j++) {
                String left = slugs.get(i);
                String right = slugs.get(j);
                double score = jaccard(cityNgrams.get(left), cityNgrams.get(right));
                rows.add(new SimilarityRow(left, right, score));
            }
        }

        List<SimilarityRow> offenders = rows.stream()
                .filter(r -> r.score() > MAX_DIFFERENTIATOR_SIMILARITY)
                .sorted(Comparator.comparingDouble(SimilarityRow::score).reversed())
                .toList();

        if (!offenders.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Near-duplicate city differentiator content detected.\n")
                    .append("Threshold: ").append(MAX_DIFFERENTIATOR_SIMILARITY).append("\n")
                    .append("Top offenders:\n");
            offenders.stream().limit(10).forEach(r -> sb
                    .append(" - ")
                    .append(r.left()).append(" vs ").append(r.right())
                    .append(" = ").append(String.format(Locale.US, "%.4f", r.score()))
                    .append("\n"));
            assertTrue(false, sb.toString());
        }
    }

    @Test
    public void narrativeFieldsShouldNotCollapseIntoTemplateSkeletons() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Path> files = Files.list(CITY_DIR)
                .filter(p -> p.toString().endsWith(".json"))
                .toList();

        for (String workLevel : List.of("part-time", "side-hustle", "full-time")) {
            for (Map.Entry<String, Double> cap : FIELD_SIMILARITY_CAPS.entrySet()) {
                String field = cap.getKey();
                double threshold = cap.getValue();

                Map<String, Set<String>> gramsByCity = new HashMap<>();
                for (Path file : files) {
                    JsonNode root = mapper.readTree(file.toFile());
                    String slug = root.path("citySlug").asText();
                    String text = root.path("workLevels").path(workLevel).path(field).asText("");
                    gramsByCity.put(slug, toNgrams(normalize(text), NGRAM_SIZE));
                }

                List<String> slugs = gramsByCity.keySet().stream().sorted().toList();
                List<SimilarityRow> offenders = new ArrayList<>();
                for (int i = 0; i < slugs.size(); i++) {
                    for (int j = i + 1; j < slugs.size(); j++) {
                        String left = slugs.get(i);
                        String right = slugs.get(j);
                        double score = jaccard(gramsByCity.get(left), gramsByCity.get(right));
                        if (score > threshold) {
                            offenders.add(new SimilarityRow(left, right, score));
                        }
                    }
                }

                if (!offenders.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Narrative field similarity exceeded cap.\n")
                            .append("workLevel=").append(workLevel).append("\n")
                            .append("field=").append(field).append("\n")
                            .append("threshold=").append(threshold).append("\n")
                            .append("Top offenders:\n");
                    offenders.stream()
                            .sorted(Comparator.comparingDouble(SimilarityRow::score).reversed())
                            .limit(10)
                            .forEach(r -> sb
                                    .append(" - ")
                                    .append(r.left()).append(" vs ").append(r.right())
                                    .append(" = ").append(String.format(Locale.US, "%.4f", r.score()))
                                    .append("\n"));
                    assertTrue(false, sb.toString());
                }
            }
        }
    }

    private String buildDifferentiatorCorpus(JsonNode root) {
        StringBuilder sb = new StringBuilder();
        sb.append(root.path("seo").path("heroHook").asText("")).append(' ');

        JsonNode workLevels = root.path("workLevels");
        for (String level : List.of("part-time", "side-hustle", "full-time")) {
            JsonNode wl = workLevels.path(level);
            sb.append(wl.path("localStrategyText").asText("")).append(' ');

            JsonNode painPoints = wl.path("painPoints");
            if (painPoints.isArray()) {
                for (JsonNode p : painPoints) {
                    sb.append(p.asText("")).append(' ');
                }
            }

            JsonNode quotes = wl.path("personaQuotes");
            if (quotes.isArray()) {
                for (JsonNode q : quotes) {
                    sb.append(q.path("displayName").asText("")).append(' ');
                    sb.append(q.path("quote").asText("")).append(' ');
                }
            }
        }

        return normalize(sb.toString());
    }

    private Set<String> toNgrams(String text, int n) {
        Set<String> grams = new HashSet<>();
        if (text == null || text.isBlank()) {
            return grams;
        }
        String[] tokens = text.split(" ");
        if (tokens.length < n) {
            return grams;
        }
        for (int i = 0; i <= tokens.length - n; i++) {
            String gram = String.join(" ", List.of(tokens).subList(i, i + n));
            grams.add(gram);
        }
        return grams;
    }

    private String normalize(String text) {
        String t = text == null ? "" : text.toLowerCase(Locale.US);
        t = TAGS.matcher(t).replaceAll(" ");
        t = NON_ALNUM.matcher(t).replaceAll(" ");
        t = MULTI_SPACE.matcher(t).replaceAll(" ").trim();
        return t;
    }

    private double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) {
            return 0.0;
        }
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / (double) union.size();
    }

    private record SimilarityRow(String left, String right, double score) {
    }
}
