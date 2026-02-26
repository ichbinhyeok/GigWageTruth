package com.gigwager.model.content;

import java.util.Map;

public record CityRichContent(
        String citySlug,
        String cityName,
        String state,
        CityCoreData coreData,
        CitySeoData seo,
        Map<String, WorkLevelRichContent> workLevels) {
}

