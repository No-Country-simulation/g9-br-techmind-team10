package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.repository.ContentRepository;
import com.g9team10.backend.shared.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ContentSearchService {

    private static final Set<String> VALID_LEVELS = Set.of("basico", "intermediario", "avancado");
    private final ContentRepository contentSearchRepository;

    public List<Content> searchByTags(List<String> tags, String level) {
        List<String> normalized = tags.stream()
                .map(TextNormalizer::normalize)
                .filter(tag -> !tag.isBlank())
                .distinct()
                .toList();

        if (normalized.isEmpty()) {
            return List.of();
        }

        String normalizedLevel = normalizeLevel(level);

        return contentSearchRepository.findByAllTagNames(normalized, normalized.size(), normalizedLevel);
    }

    private String normalizeLevel(String level) {
        if (level == null || level.isBlank()) {
            return null;
        }
        String normalizedLevel = TextNormalizer.normalize(level);
        if (!VALID_LEVELS.contains(normalizedLevel)) {
            throw new IllegalArgumentException("Nivel invalido. Use: basico, intermediario ou avancado.");
        }
        return normalizedLevel;
    }
}