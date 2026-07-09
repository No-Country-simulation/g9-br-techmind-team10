package com.g9team10.backend.service;

import com.g9team10.backend.dto.ModelPredictRequestDTO;
import com.g9team10.backend.dto.ModelPredictResponseDTO;

public interface ModelPredictionService {

    ModelPredictResponseDTO predict(ModelPredictRequestDTO request);
}
