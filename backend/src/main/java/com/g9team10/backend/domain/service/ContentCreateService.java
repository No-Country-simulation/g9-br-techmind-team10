package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.valueObject.ModelPredictRequest;
import com.g9team10.backend.domain.model.valueObject.ModelPredictResult;
import com.g9team10.backend.domain.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentCreateService {

    private final ModelPredictionService modelPredictionService;
    private final LevelClassificationService levelClassificationService;
    private final ContentRepository contentRepository;
    private final TagService tagService;

    @Transactional
    public Content create(String title, String text) {
        ModelPredictResult prediction = modelPredictionService.predict(new ModelPredictRequest(title, text));

        Content content = new Content(title, text, prediction.category(), prediction.probability());

        content.classify(levelClassificationService.classify(title, text));

        content.addTags(tagService.resolve(prediction.tags()));

        content.created();

        return contentRepository.save(content);
    }
}