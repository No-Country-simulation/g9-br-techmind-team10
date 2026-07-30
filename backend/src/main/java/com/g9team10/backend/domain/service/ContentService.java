package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.ContentChunk;
import com.g9team10.backend.domain.model.Level;
import com.g9team10.backend.domain.model.Tag;
import com.g9team10.backend.domain.model.valueObject.ModelPredictRequest;
import com.g9team10.backend.domain.model.valueObject.ModelPredictResult;
import com.g9team10.backend.domain.repository.*;
import com.g9team10.backend.shared.TextChunker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final ModelPredictionService modelPredictionService;
    private final LevelClassificationService levelClassificationService;
    private final ContentRepository contentRepository;
    private final ContentChunkRepository contentChunkRepository;
    private final EmbeddingGateway embeddingGateway;
    private final TagService tagService;

    @Transactional
    public Content analysis(String title, String text) {
        ModelPredictRequest predictRequest = new ModelPredictRequest(title, text);
        ModelPredictResult response = modelPredictionService.predict(predictRequest);

        Content content = new Content(title, text, response.category(), response.probability());
        Level level = levelClassificationService.classify(title, text);
        content.setLevel(level);
        Set<Tag> tags = tagService.resolve(response.tags());

        content.addTags(tags);
        content = contentRepository.save(content);

        List<String> chunks = TextChunker.split(content.getText());
        for (int i = 0; i < chunks.size(); i++) {
            ContentChunk chunk = contentChunkRepository.save(new ContentChunk(content, i, chunks.get(i)));
            embeddingGateway.generateEmbedding(chunk.getId(), chunk.getText());
        }

        embeddingGateway.generateCentroid(content.getId());

        return content;
    }
}