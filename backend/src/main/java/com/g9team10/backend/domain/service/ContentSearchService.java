package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.Level;
import com.g9team10.backend.domain.model.valueObject.SimilarContent;
import com.g9team10.backend.domain.repository.ContentRepository;
import com.g9team10.backend.domain.repository.SemanticSearchRepository;
import com.g9team10.backend.shared.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContentSearchService {

    private final ContentRepository contentSearchRepository;
    private final SemanticSearchRepository semanticSearchRepository;

    public List<Content> searchByTags(List<String> tags, Level level) {
        List<String> normalized = tags.stream()
                .map(TextNormalizer::normalize)
                .filter(tag -> !tag.isBlank())
                .distinct()
                .toList();

        if (normalized.isEmpty()) {
            return List.of();
        }

        return contentSearchRepository.findByAllTagNames(normalized, normalized.size(), level);
    }

    public List<SimilarContent> searchSimilar(String q, Integer limit) {
        return semanticSearchRepository.searchSimilarContent(q, limit);
    }

    public List<SimilarContent> searchRecommendations(Long id, Integer limit) {
        return semanticSearchRepository.searchRecommendations(id, limit);
    }

}