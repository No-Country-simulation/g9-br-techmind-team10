package com.g9team10.backend.dto;

import com.g9team10.backend.model.Content;

public record ContentSummaryDTO(Long id, String title, String category, String level) {
    public static ContentSummaryDTO fromEntity(Content content) {
        return new ContentSummaryDTO(content.getId(), content.getTitle(), content.getCategory(), content.getLevel());
    }
}
