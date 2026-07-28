package com.g9team10.backend.api.dto.response;

import com.g9team10.backend.core.config.TrustPropertiesConfig;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.Tag;

import java.time.OffsetDateTime;
import java.util.List;

public record ContentDetailDTO(
        Long id,
        String title,
        String text,
        String category,
        String level,
        Double probability,
        Boolean lowConfidenceAlert,
        Boolean revised,
        OffsetDateTime dateProcessing,
        List<String> tags
) {
    public static ContentDetailDTO fromEntity(Content content, TrustPropertiesConfig trustProperties) {
        return new ContentDetailDTO(
                content.getId(),
                content.getTitle(),
                content.getText(),
                content.getCategory(),
                content.getLevel(),
                content.getProbability(),
                trustProperties.isLowConfidence(content.getProbability()),
                content.getRevised(),
                content.getDateProcessing(),
                content.getTags().stream()
                        .map(Tag::getName)
                        .toList()
        );
    }
}