package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Level;

public interface LevelClassificationService {
    Level classify(String title, String text);
}