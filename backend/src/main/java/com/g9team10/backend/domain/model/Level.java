package com.g9team10.backend.domain.model;

import lombok.Getter;

import java.text.Normalizer;
import java.util.Locale;

@Getter
public enum Level {
    BASIC,
    INTERMEDIATE,
    ADVANCED;

    public static Level from(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidLevelException(value);
        }

        try {
            return Level.valueOf(normalize(value));
        } catch (IllegalArgumentException ex) {
            throw new InvalidLevelException(value);
        }
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z]", "")
                .toUpperCase(Locale.ROOT);
    }
}
