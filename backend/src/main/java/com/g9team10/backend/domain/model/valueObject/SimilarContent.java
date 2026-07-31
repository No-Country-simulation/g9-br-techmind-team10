package com.g9team10.backend.domain.model.valueObject;

public record SimilarContent(
        Long id,
        String title,
        String content,
        String category,
        Double distance
) {
}
