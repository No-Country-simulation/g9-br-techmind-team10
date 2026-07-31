package com.g9team10.backend.domain.repository;

import com.g9team10.backend.domain.exception.ContentNotFoundException;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.Level;
import com.g9team10.backend.domain.model.valueObject.CategoryCount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @EntityGraph(attributePaths = "tags")
    @Query("""
        SELECT c FROM Content c
        WHERE c.id IN (
            SELECT c2.id FROM Content c2
            JOIN c2.tags t2
            WHERE t2.name IN :tags
            GROUP BY c2.id
            HAVING COUNT(DISTINCT t2.name) = :qtdTags
        )
        AND (:level IS NULL OR c.level = :level)
        ORDER BY c.dateProcessing DESC
    """)
    List<Content> findByAllTagNames(@Param("tags") List<String> tags, @Param("qtdTags") long qtdTags, @Param("level") Level level);

    @Query("""
        SELECT new com.g9team10.backend.domain.model.valueObject.CategoryCount(
            c.category,
            COUNT(c)
        )
        FROM Content c
        GROUP BY c.category
        ORDER BY c.category
    """)
    List<CategoryCount> countByCategory();

    default Content findRequired(Long id) {
        return findById(id)
                .orElseThrow(() -> new ContentNotFoundException(id));
    }
}