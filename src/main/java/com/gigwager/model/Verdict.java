package com.gigwager.model;

import java.util.List;

public record Verdict(
        String level, // LOSS, SURVIVAL, WORKABLE, OPTIMIZATION
        String headline,
        List<String> paragraphs,
        String cta,
        String themeColor // red, orange, yellow, emerald
) {
}
