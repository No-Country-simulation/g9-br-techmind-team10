package com.g9team10.backend.dto;

import com.g9team10.backend.model.UserContentTag;

public record UserContentTagResponseDTO(
        Long id,
        String name,
        String normalizedName,
        Long contentId
) {
    public static UserContentTagResponseDTO fromEntity(UserContentTag tag) {
        return new UserContentTagResponseDTO(
                tag.getId(),
                tag.getName(),
                tag.getNormalizedName(),
                tag.getContent().getId()
        );
    }
}