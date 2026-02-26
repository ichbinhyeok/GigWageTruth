package com.gigwager.model.content;

import java.util.List;

public record WorkLevelRichContent(
        NumericRange realisticNetHourlyRange,
        String localStrategyText,
        String workLevelMeaningHtml,
        String taxStrategyHtml,
        String dayInTheLifeHtml,
        String bestPracticesHtml,
        List<String> painPoints,
        List<PersonaQuote> personaQuotes) {
}

