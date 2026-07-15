package com.g9team10.backend.service;

import com.g9team10.backend.model.Content;
import com.g9team10.backend.model.History;
import com.g9team10.backend.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HistoryService {

    private final ContentService contentService;
    private final HistoryRepository historyRepository;

    @Transactional
    public void registerView(User user, Long contentId) {
        Content content = contentService.find(contentId);

        historyRepository.save(new History(user, content));
    }
}
