package com.g9team10.backend.service;

import com.g9team10.backend.dto.UserContentTagRequestDTO;
import com.g9team10.backend.dto.UserContentTagSummaryDTO;
import com.g9team10.backend.exception.InvalidUserContentTagException;
import com.g9team10.backend.exception.UserContentTagNotFoundException;
import com.g9team10.backend.model.Content;
import com.g9team10.backend.model.User;
import com.g9team10.backend.model.UserContentTag;
import com.g9team10.backend.repository.UserContentTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserContentTagService {

    private final UserContentTagRepository userContentTagRepository;
    private final ContentService contentService;

    public List<UserContentTag> listByContent(User user, Long contentId) {
        contentService.find(contentId);

        return userContentTagRepository.findByUserIdAndContentIdOrderByCreatedAtAsc(
                user.getId(),
                contentId
        );
    }

    @Transactional
    public UserContentTag create(User user, Long contentId, UserContentTagRequestDTO request) {
        Content content = contentService.find(contentId);

        String displayName = normalizeDisplayName(request.name());
        String normalizedName = normalizeTagKey(displayName);

        return userContentTagRepository
                .findByUserIdAndContentIdAndNormalizedName(user.getId(), contentId, normalizedName)
                .orElseGet(() -> userContentTagRepository.save(
                        new UserContentTag(user, content, displayName, normalizedName)
                ));
    }

    @Transactional
    public void delete(User user, Long contentId, Long tagId) {
        UserContentTag tag = userContentTagRepository
                .findByIdAndUserIdAndContentId(tagId, user.getId(), contentId)
                .orElseThrow(() -> new UserContentTagNotFoundException(tagId));

        userContentTagRepository.delete(tag);
    }

    public List<UserContentTagSummaryDTO> listUserTags(User user) {
        List<UserContentTag> tags = userContentTagRepository.findByUserIdOrderByNormalizedNameAscNameAsc(user.getId());

        Map<String, UserContentTagSummaryAccumulator> groupedTags = new LinkedHashMap<>();

        for (UserContentTag tag : tags) {
            groupedTags
                    .computeIfAbsent(
                            tag.getNormalizedName(),
                            normalizedName -> new UserContentTagSummaryAccumulator(
                                    tag.getName(),
                                    tag.getNormalizedName()
                            )
                    )
                    .increment();
        }

        return groupedTags.values()
                .stream()
                .map(UserContentTagSummaryAccumulator::toDTO)
                .toList();
    }

    public List<Content> searchContentsByPersonalTags(User user, List<String> tags) {
        List<String> normalizedNames = tags.stream()
                .map(this::normalizeDisplayName)
                .map(this::normalizeTagKey)
                .distinct()
                .toList();

        if (normalizedNames.isEmpty()) {
            return List.of();
        }

        return userContentTagRepository.findContentsByUserIdAndAllNormalizedNames(
                user.getId(),
                normalizedNames,
                normalizedNames.size()
        );
    }

    private String normalizeDisplayName(String value) {
        String displayName = value == null ? "" : value.trim().replaceAll("\\s+", " ");

        if (displayName.isBlank()) {
            throw new InvalidUserContentTagException();
        }

        return displayName;
    }

    private String normalizeTagKey(String value) {
        String normalizedValue = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        if (normalizedValue.isBlank()) {
            throw new InvalidUserContentTagException();
        }

        return normalizedValue;
    }

    private static class UserContentTagSummaryAccumulator {

        private final String name;
        private final String normalizedName;
        private long total = 0;

        private UserContentTagSummaryAccumulator(String name, String normalizedName) {
            this.name = name;
            this.normalizedName = normalizedName;
        }

        private void increment() {
            total++;
        }

        private UserContentTagSummaryDTO toDTO() {
            return new UserContentTagSummaryDTO(name, normalizedName, total);
        }
    }
}