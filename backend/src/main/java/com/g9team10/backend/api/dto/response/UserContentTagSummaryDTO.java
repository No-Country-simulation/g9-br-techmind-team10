package com.g9team10.backend.api.dto.response;

public record UserContentTagSummaryDTO(
        String name,
        String normalizedName,
        Long total
) {
}