package com.g9team10.backend.api.dto.response;

import java.util.List;

public record ModelPredictResponseDTO(String category, Double probability, List<String> tags) {
}
