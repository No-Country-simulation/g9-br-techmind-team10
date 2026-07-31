package com.g9team10.backend.api.dto.response;

import com.g9team10.backend.domain.model.UserContentTag;

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