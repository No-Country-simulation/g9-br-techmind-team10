package com.g9team10.backend.domain.model;

import com.g9team10.backend.domain.exception.InvalidLevelException;
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

    public static Level fromNullable(String value) {
        return (value == null || value.isBlank())
                ? null
                : from(value);
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z]", "")
                .toUpperCase(Locale.ROOT);
    }
}
