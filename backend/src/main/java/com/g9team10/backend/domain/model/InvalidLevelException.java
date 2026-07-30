package com.g9team10.backend.domain.model;

import com.g9team10.backend.domain.exception.BusinessException;

public class InvalidLevelException extends BusinessException {
    public InvalidLevelException(String message) {
        super(message);
    }
}
