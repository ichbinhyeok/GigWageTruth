package com.gigwager.model;

/**
 * A dated, externally published city-pay figure shown next to GigVerdict's model.
 * The definition is intentionally explicit because gross, tracked, active-time,
 * and after-expense hourly figures are not interchangeable.
 */
public record MarketPayEvidence(
        String evidenceType,
        String sourceName,
        double hourlyPay,
        String payLabel,
        String marketCoverage,
        String definition,
        String observedAt,
        String sourceUrl) {
}
