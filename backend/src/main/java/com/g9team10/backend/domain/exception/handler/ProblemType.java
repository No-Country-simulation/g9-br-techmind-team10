package com.g9team10.backend.domain.exception.handler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public enum ProblemType {

    INVALID_REQUEST("/invalid-request", "Invalid request"),
    BUSINESS_ERROR("/business-error", "Business error"),
    INVALID_CREDENTIALS("/invalid-credentials", "Invalid credentials"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    INCOMPREHENSIBLE_MESSAGE("/incomprehensible-message", "Incomprehensible message"),
    ;

    private final String title;
    private final String path;

    ProblemType(String path, String title) {
        this.path = path;
        this.title = title;
    }
}
