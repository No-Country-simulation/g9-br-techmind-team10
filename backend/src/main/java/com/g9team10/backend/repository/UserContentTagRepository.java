package com.g9team10.backend.repository;

import com.g9team10.backend.model.Content;
import com.g9team10.backend.model.UserContentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserContentTagRepository extends JpaRepository<UserContentTag, Long> {

    List<UserContentTag> findByUserIdAndContentIdOrderByCreatedAtAsc(Long userId, Long contentId);

    List<UserContentTag> findByUserIdOrderByNormalizedNameAscNameAsc(Long userId);

    Optional<UserContentTag> findByUserIdAndContentIdAndNormalizedName(
            Long userId,
            Long contentId,
            String normalizedName
    );

    Optional<UserContentTag> findByIdAndUserIdAndContentId(
            Long id,
            Long userId,
            Long contentId
    );

    @Query("""
        SELECT c FROM Content c
        WHERE c.id IN (
            SELECT tag.content.id FROM UserContentTag tag
            WHERE tag.user.id = :userId
            AND tag.normalizedName IN :normalizedNames
            GROUP BY tag.content.id
            HAVING COUNT(DISTINCT tag.normalizedName) = :tagCount
        )
        ORDER BY c.dateProcessing DESC
    """)
    List<Content> findContentsByUserIdAndAllNormalizedNames(
            Long userId,
            List<String> normalizedNames,
            long tagCount
    );
}