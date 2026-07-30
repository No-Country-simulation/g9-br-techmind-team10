package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.exception.UserContentTagNotFoundException;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.model.UserContentTag;
import com.g9team10.backend.domain.model.valueObject.UserContentTagSummary;
import com.g9team10.backend.domain.repository.UserContentTagRepository;
import com.g9team10.backend.shared.TextNormalizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
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
    public UserContentTag create(User user, Long contentId, String tagName) {
        Content content = contentService.find(contentId);

        UserContentTag tag = UserContentTag.create(user, content, tagName);

        return userContentTagRepository
                .findByUserIdAndContentIdAndNormalizedName(user.getId(), contentId, tag.getNormalizedName())
                .orElseGet(() -> userContentTagRepository.save(tag));
    }

    @Transactional
    public void delete(User user, Long contentId, Long tagId) {
        UserContentTag tag = userContentTagRepository
                .findByIdAndUserIdAndContentId(tagId, user.getId(), contentId)
                .orElseThrow(() -> new UserContentTagNotFoundException(tagId));

        userContentTagRepository.delete(tag);
    }

    public List<UserContentTagSummary> listUserTags(User user) {
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
                .map(UserContentTagSummaryAccumulator::toSummary)
                .toList();
    }

    public List<Content> searchContentsByPersonalTags(User user, List<String> tags) {
        List<String> normalizedNames = tags.stream()
                .map(UserContentTag::normalizeDisplayName)
                .map(TextNormalizer::normalize)
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

        private UserContentTagSummary toSummary() {
            return new UserContentTagSummary(name, normalizedName, total);
        }
    }
}