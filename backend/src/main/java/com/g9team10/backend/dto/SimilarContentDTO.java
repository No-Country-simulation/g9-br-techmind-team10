package com.g9team10.backend.dto;

public record SimilarContentDTO(
        Long id,
        String title,
        String content,
        String category,
        Double distance
) {
}
