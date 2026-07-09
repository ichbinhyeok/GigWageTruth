package com.gigwager.model;

public record PageEvidenceProfile(
        String heading,
        String confidenceLabel,
        String confidenceTone,
        String summary,
        int sourceCount,
        int driverReportCount,
        int citySpecificDriverReportCount,
        String lastVerifiedAt,
        String methodologyVersion,
        String uniqueDataAnchor,
        boolean richCitedContent,
        boolean indexable) {
}
