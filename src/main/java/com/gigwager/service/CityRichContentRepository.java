package com.gigwager.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigwager.model.content.CityRichContent;
import com.gigwager.model.content.CitySeoData;
import com.gigwager.model.content.PersonaQuote;
import com.gigwager.model.content.WorkLevelRichContent;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CityRichContentRepository {

    private static final Set<String> REQUIRED_WORK_LEVELS = Set.of("part-time", "side-hustle", "full-time");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "editorial_composite",
            "user_submitted",
            "verified_interview");

    private final ObjectMapper objectMapper;
    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private Map<String, CityRichContent> cityContentBySlug = Map.of();

    public CityRichContentRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @PostConstruct
    public void init() {
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources("classpath:data/cities/*.json");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to scan city rich content files", e);
        }

        if (resources.length == 0) {
            throw new IllegalStateException("No city rich content files found under classpath:data/cities/*.json");
        }

        Map<String, CityRichContent> loaded = new HashMap<>();
        for (Resource resource : resources) {
            CityRichContent content = parse(resource);
            validate(content, resource.getFilename());
            if (loaded.putIfAbsent(content.citySlug(), content) != null) {
                throw new IllegalStateException("Duplicate citySlug in rich content: " + content.citySlug());
            }
        }

        cityContentBySlug = Collections.unmodifiableMap(loaded);
    }

    public Optional<CityRichContent> findBySlug(String citySlug) {
        if (citySlug == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(cityContentBySlug.get(citySlug.toLowerCase(Locale.US)));
    }

    public boolean hasWorkLevelContent(String citySlug, String workLevelSlug) {
        Optional<CityRichContent> city = findBySlug(citySlug);
        return city.isPresent() && city.get().workLevels() != null && city.get().workLevels().containsKey(workLevelSlug);
    }

    public Collection<CityRichContent> findAll() {
        return cityContentBySlug.values();
    }

    private CityRichContent parse(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, CityRichContent.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse rich content: " + resource.getFilename(), e);
        }
    }

    private void validate(CityRichContent city, String fileName) {
        List<String> errors = new ArrayList<>();

        if (city == null) {
            throw new IllegalStateException("Rich content file " + fileName + " is empty or invalid JSON object");
        }

        String expectedSlug = fileName == null ? null : fileName.replace(".json", "");
        if (isBlank(city.citySlug())) {
            errors.add("citySlug is required");
        } else if (expectedSlug != null && !city.citySlug().equals(expectedSlug)) {
            errors.add("citySlug must match filename. expected=" + expectedSlug + ", actual=" + city.citySlug());
        }

        if (isBlank(city.cityName())) {
            errors.add("cityName is required");
        }
        if (isBlank(city.state())) {
            errors.add("state is required");
        }

        if (city.coreData() == null) {
            errors.add("coreData is required");
        } else {
            if (city.coreData().gasPrice() == null || city.coreData().gasPrice() <= 0) {
                errors.add("coreData.gasPrice must be > 0");
            }
            if (city.coreData().trafficFactor() == null || city.coreData().trafficFactor() <= 0) {
                errors.add("coreData.trafficFactor must be > 0");
            }
        }

        CitySeoData seo = city.seo();
        if (seo == null) {
            errors.add("seo is required");
        } else {
            if (isBlank(seo.heroHook()) || seo.heroHook().length() < 60) {
                errors.add("seo.heroHook must be at least 60 characters");
            }
            if (seo.pageStructureType() == null) {
                errors.add("seo.pageStructureType is required");
            }
            if (isBlank(seo.contentType()) || !ALLOWED_CONTENT_TYPES.contains(seo.contentType().toLowerCase(Locale.US))) {
                errors.add("seo.contentType must be one of " + ALLOWED_CONTENT_TYPES);
            }
            if (seo.contentType() != null && seo.contentType().toLowerCase(Locale.US).contains("verified_true")) {
                errors.add("seo.contentType cannot claim verified_true");
            }
            if (isBlank(seo.methodologyVersion())) {
                errors.add("seo.methodologyVersion is required");
            }
            if (isBlank(seo.lastVerifiedAt())) {
                errors.add("seo.lastVerifiedAt is required");
            } else {
                try {
                    LocalDate.parse(seo.lastVerifiedAt());
                } catch (Exception e) {
                    errors.add("seo.lastVerifiedAt must be ISO-8601 date (yyyy-MM-dd)");
                }
            }
            if (seo.sources() == null || seo.sources().size() < 2) {
                errors.add("seo.sources must include at least 2 citations");
            } else {
                for (int i = 0; i < seo.sources().size(); i++) {
                    var source = seo.sources().get(i);
                    if (source == null) {
                        errors.add("seo.sources[" + i + "] cannot be null");
                        continue;
                    }
                    if (isBlank(source.title())) {
                        errors.add("seo.sources[" + i + "].title is required");
                    }
                    if (isBlank(source.url()) || !(source.url().startsWith("http://") || source.url().startsWith("https://"))) {
                        errors.add("seo.sources[" + i + "].url must start with http:// or https://");
                    }
                    if (isBlank(source.publisher())) {
                        errors.add("seo.sources[" + i + "].publisher is required");
                    }
                    if (isBlank(source.checkedAt())) {
                        errors.add("seo.sources[" + i + "].checkedAt is required");
                    }
                }
            }
        }

        if (city.workLevels() == null || city.workLevels().isEmpty()) {
            errors.add("workLevels is required");
        } else {
            Set<String> available = city.workLevels().keySet();
            for (String required : REQUIRED_WORK_LEVELS) {
                if (!available.contains(required)) {
                    errors.add("workLevels missing required key: " + required);
                    continue;
                }
                validateWorkLevel(required, city.workLevels().get(required), errors);
            }
        }

        if (!errors.isEmpty()) {
            String message = "Invalid rich content file: " + fileName + "\n - "
                    + errors.stream().filter(Objects::nonNull).collect(Collectors.joining("\n - "));
            throw new IllegalStateException(message);
        }
    }

    private void validateWorkLevel(String workLevelSlug, WorkLevelRichContent workLevel, List<String> errors) {
        String prefix = "workLevels." + workLevelSlug;
        if (workLevel == null) {
            errors.add(prefix + " must not be null");
            return;
        }

        if (workLevel.realisticNetHourlyRange() == null
                || workLevel.realisticNetHourlyRange().min() == null
                || workLevel.realisticNetHourlyRange().max() == null) {
            errors.add(prefix + ".realisticNetHourlyRange min/max are required");
        } else {
            double min = workLevel.realisticNetHourlyRange().min();
            double max = workLevel.realisticNetHourlyRange().max();
            if (min <= 0 || max <= 0 || min > max) {
                errors.add(prefix + ".realisticNetHourlyRange must satisfy 0 < min <= max");
            }
            if (min < 4 || max > 80) {
                errors.add(prefix + ".realisticNetHourlyRange should be within practical bounds (4-80)");
            }
        }

        if (isBlank(workLevel.localStrategyText()) || workLevel.localStrategyText().length() < 80) {
            errors.add(prefix + ".localStrategyText must be at least 80 characters");
        }
        if (containsPlaceholder(workLevel.localStrategyText())) {
            errors.add(prefix + ".localStrategyText contains placeholder token");
        }

        validateHtmlBlock(prefix + ".workLevelMeaningHtml", workLevel.workLevelMeaningHtml(), errors);
        validateHtmlBlock(prefix + ".taxStrategyHtml", workLevel.taxStrategyHtml(), errors);
        validateHtmlBlock(prefix + ".dayInTheLifeHtml", workLevel.dayInTheLifeHtml(), errors);
        validateHtmlBlock(prefix + ".bestPracticesHtml", workLevel.bestPracticesHtml(), errors);

        if (workLevel.painPoints() == null || workLevel.painPoints().size() < 2) {
            errors.add(prefix + ".painPoints must include at least 2 entries");
        } else if (workLevel.painPoints().stream().anyMatch(this::containsPlaceholder)) {
            errors.add(prefix + ".painPoints contains placeholder token");
        }

        if (workLevel.personaQuotes() == null || workLevel.personaQuotes().size() < 2) {
            errors.add(prefix + ".personaQuotes must include at least 2 quotes");
        } else {
            Set<String> uniquePersonaTypes = workLevel.personaQuotes().stream()
                    .map(PersonaQuote::personaType)
                    .filter(Objects::nonNull)
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            if (uniquePersonaTypes.size() < 2) {
                errors.add(prefix + ".personaQuotes must contain at least 2 different persona types");
            }
            for (int i = 0; i < workLevel.personaQuotes().size(); i++) {
                PersonaQuote quote = workLevel.personaQuotes().get(i);
                if (quote == null) {
                    errors.add(prefix + ".personaQuotes[" + i + "] cannot be null");
                    continue;
                }
                if (quote.personaType() == null) {
                    errors.add(prefix + ".personaQuotes[" + i + "].personaType is required");
                }
                if (isBlank(quote.displayName()) || quote.displayName().length() < 3) {
                    errors.add(prefix + ".personaQuotes[" + i + "].displayName is required");
                }
                if (isBlank(quote.quote()) || quote.quote().length() < 30) {
                    errors.add(prefix + ".personaQuotes[" + i + "].quote must be at least 30 characters");
                }
                if (quote.attributionType() == null) {
                    errors.add(prefix + ".personaQuotes[" + i + "].attributionType is required");
                }
                if (containsPlaceholder(quote.quote())) {
                    errors.add(prefix + ".personaQuotes[" + i + "].quote contains placeholder token");
                }
            }
        }
    }

    private void validateHtmlBlock(String field, String html, List<String> errors) {
        if (isBlank(html) || html.length() < 80) {
            errors.add(field + " must be at least 80 characters");
        }
        if (containsPlaceholder(html)) {
            errors.add(field + " contains placeholder token");
        }
    }

    private boolean containsPlaceholder(String value) {
        return value != null && value.matches(".*\\[[^\\]]+\\].*");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

