package com.gigwager.model;

public record SearchResultPattern(
                String resultType,
                String competitor,
                String winningSignal,
                String missingCheck,
                String gigVerdictResponse,
                String sourceLabel,
                String sourceUrl) {
}
