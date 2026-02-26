package com.gigwager.model.content;

import java.util.List;

public record CitySeoData(
        String heroHook,
        PageStructureType pageStructureType,
        String contentType,
        String methodologyVersion,
        String lastVerifiedAt,
        List<SourceCitation> sources) {
}

