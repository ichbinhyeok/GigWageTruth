package com.gigwager.model.content;

public record PersonaQuote(
        PersonaType personaType,
        String displayName,
        String quote,
        AttributionType attributionType) {
}

