package com.g9team10.backend.dto;

public record UserContentTagSummaryDTO(
        String name,
        String normalizedName,
        Long total
) {
}