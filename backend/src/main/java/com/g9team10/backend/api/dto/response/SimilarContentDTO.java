package com.g9team10.backend.api.dto.response;

public record SimilarContentDTO(
        Long id,
        String title,
        String content,
        String category,
        Double distance
) {
}
