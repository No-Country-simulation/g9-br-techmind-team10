package com.g9team10.backend.api.dto.response;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.Tag;

import java.util.List;

public record ContentResponseDTO(String category,
                                 Double probability,
                                 List<String> additionalInformation,
                                 String level) {
    public static ContentResponseDTO fromEntity(Content content) {
        return new ContentResponseDTO(
                content.getCategory(),
                content.getProbability(),
                content.getTags().stream().map(Tag::getName).toList(),
                content.getLevel()
        );
    }
}
