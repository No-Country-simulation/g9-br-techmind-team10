package com.g9team10.backend.domain.exception;

public class ModelPredictUnavailableException extends BusinessException {
    public ModelPredictUnavailableException(Exception e) {
        super("Failed to load model predict due to " + e.getMessage());
    }
}
