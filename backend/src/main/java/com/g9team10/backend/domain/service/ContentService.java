package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.exception.ContentNotFoundException;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.ContentChunk;
import com.g9team10.backend.domain.model.Tag;
import com.g9team10.backend.domain.model.valueObject.ModelPredictRequest;
import com.g9team10.backend.domain.model.valueObject.ModelPredictResult;
import com.g9team10.backend.domain.model.valueObject.SimilarContent;
import com.g9team10.backend.domain.repository.*;
import com.g9team10.backend.shared.TextChunker;
import com.g9team10.backend.shared.TextNormalizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final ModelPredictionService modelPredictionService;
    private final LevelClassificationService levelClassificationService;
    private final TagRepository tagRepository;
    private final ContentRepository contentRepository;
    private final ContentChunkRepository contentChunkRepository;
    private final SemanticSearchRepository semanticSearchRepository;
    private final EmbeddingGateway embeddingGateway;

    @Transactional
    public Content analysis(String title, String text) {
        ModelPredictRequest predictRequest = new ModelPredictRequest(title, text);
        ModelPredictResult response = modelPredictionService.predict(predictRequest);

        Content content = new Content(title, text, response.category(), response.probability());
        String level = levelClassificationService.classify(title, text);
        content.setLevel(level);
        List<String> tags = response.tags();

        if (tags != null) {
            for (String grossValue : tags) {
                String normalizedValue = TextNormalizer.normalize(grossValue);

                Tag tag = findOrCreateTag(normalizedValue);

                content.addTag(tag);
            }
        }

        content = contentRepository.save(content);

        List<String> chunks = TextChunker.split(content.getText());
        for (int i = 0; i < chunks.size(); i++) {
            ContentChunk chunk = contentChunkRepository.save(new ContentChunk(content, i, chunks.get(i)));
            embeddingGateway.generateEmbedding(chunk.getId(), chunk.getText());
        }

        embeddingGateway.generateCentroid(content.getId());

        return content;
    }

    public Content find(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException(contentId));
    }

    @Transactional
    public Content fixTags(Long id, List<String> fixedTags) {
        Content content = find(id);

        Set<Tag> normalizeTags = fixedTags.stream()
                .map(TextNormalizer::normalize)
                .filter(tag -> !tag.isBlank())
                .distinct()
                .map(this::findOrCreateTag)
                .collect(Collectors.toSet());

        content.getTags().clear();
        content.getTags().addAll(normalizeTags);
        content.review();

        return contentRepository.save(content);
    }

    @Transactional
    public Content confirmTags(Long id) {
        Content content = find(id);
        content.review();

        return contentRepository.save(content);
    }

    public List<SimilarContent> searchSimilar(String q, Integer limit) {
        return semanticSearchRepository.searchSimilarContent(q, limit);
    }

    public List<SimilarContent> searchRecommendations(Long id, Integer limit) {
        return semanticSearchRepository.searchRecommendations(id, limit);
    }

    private Tag findOrCreateTag(String normalizedValue) {
        return tagRepository.findByName(normalizedValue)
                .orElseGet(() -> tagRepository.save(new Tag(normalizedValue)));
    }
}