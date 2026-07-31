package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.Favorite;
import com.g9team10.backend.domain.model.FavoriteId;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.repository.ContentRepository;
import com.g9team10.backend.domain.repository.FavoriteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public void check(User user, Long contentId) {
        Content content = contentRepository.findRequired(contentId);

        FavoriteId id = new FavoriteId(user.getId(), content.getId());
        if (favoriteRepository.existsById(id)) {
            return;
        }

        favoriteRepository.save(new Favorite(user, content));
    }

    @Transactional
    public void uncheck(User user, Long contentId) {
        Content content = contentRepository.findRequired(contentId);

        FavoriteId id = new FavoriteId(user.getId(), content.getId());
        favoriteRepository.deleteById(id);
    }

    public List<Content> list(Long userId) {
        return favoriteRepository.findByUser(userId)
                .stream()
                .map(Favorite::getContent)
                .toList();
    }
}
