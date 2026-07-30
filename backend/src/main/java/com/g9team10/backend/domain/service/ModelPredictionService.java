package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.valueObject.ModelPredictRequest;
import com.g9team10.backend.domain.model.valueObject.ModelPredictResult;

public interface ModelPredictionService {

    ModelPredictResult predict(ModelPredictRequest request);
}
