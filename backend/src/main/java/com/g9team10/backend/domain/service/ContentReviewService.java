package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.Tag;
import com.g9team10.backend.domain.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ContentReviewService {

    private final ContentRepository contentRepository;
    private final TagService tagService;

    @Transactional
    public Content confirmTags(Long id) {
        Content content = contentRepository.findRequired(id);
        content.review();

        return contentRepository.save(content);
    }

    @Transactional
    public Content fixTags(Long id, List<String> fixedTags) {
        Content content = contentRepository.findRequired(id);

        Set<Tag> tags = tagService.resolve(fixedTags);

        content.replaceTags(tags);
        content.review();

        return contentRepository.save(content);
    }

}
