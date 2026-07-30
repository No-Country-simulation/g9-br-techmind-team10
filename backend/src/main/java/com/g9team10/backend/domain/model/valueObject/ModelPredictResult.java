package com.g9team10.backend.domain.model.valueObject;

import java.util.List;

public record ModelPredictResult(String category, Double probability, List<String> tags) {
}
