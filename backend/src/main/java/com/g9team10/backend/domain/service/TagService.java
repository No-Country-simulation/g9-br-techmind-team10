package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Tag;
import com.g9team10.backend.domain.repository.TagRepository;
import com.g9team10.backend.shared.TextNormalizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Set<Tag> resolve(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Set.of();
        }

        return values.stream()
                .map(TextNormalizer::normalize)
                .filter(tag -> !tag.isBlank())
                .distinct()
                .map(this::findOrCreate)
                .collect(Collectors.toSet());
    }

    private Tag findOrCreate(String normalizedValue) {
        return tagRepository.findByName(normalizedValue)
                .orElseGet(() -> tagRepository.save(new Tag(normalizedValue)));
    }
}
