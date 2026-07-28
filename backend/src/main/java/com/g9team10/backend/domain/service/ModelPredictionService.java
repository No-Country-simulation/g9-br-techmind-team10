package com.g9team10.backend.domain.service;

import com.g9team10.backend.api.dto.request.ModelPredictRequestDTO;
import com.g9team10.backend.api.dto.response.ModelPredictResponseDTO;

public interface ModelPredictionService {

    ModelPredictResponseDTO predict(ModelPredictRequestDTO request);
}
