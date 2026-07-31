package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.repository.ContentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentViewService {

    private final ContentRepository contentRepository;
    private final HistoryService historyService;

    @Transactional
    public Content view(User user, Long contentId) {
        Content content = contentRepository.findRequired(contentId);
        historyService.registerView(user, contentId);
        return content;
    }
}
