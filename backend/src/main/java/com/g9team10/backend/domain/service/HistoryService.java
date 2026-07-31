package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.History;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.repository.ContentRepository;
import com.g9team10.backend.domain.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public void registerView(User user, Long contentId) {
        Content content = contentRepository.findRequired(contentId);

        historyRepository.save(new History(user, content));
    }

    public List<Content> list(Long userId) {
        return historyRepository.findRecentByUser(userId)
                .stream()
                .map(History::getContent)
                .toList();
    }
}
