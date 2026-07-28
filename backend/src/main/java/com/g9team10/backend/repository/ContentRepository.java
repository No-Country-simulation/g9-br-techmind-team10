package com.g9team10.backend.repository;

import com.g9team10.backend.dto.ContentCountDTO;
import com.g9team10.backend.model.Content;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE content c
            SET embedding_centroid = (
                SELECT AVG(cc.embedding)
                FROM content_chunk cc
                WHERE cc.content_id = c.id
            )
            WHERE c.id = :id
            """, nativeQuery = true)
    void generateCentroid(@Param("id") Long id);

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
    List<Content> findByAllTagNames(@Param("tags") List<String> tags, @Param("qtdTags") long qtdTags, @Param("level") String level);

    @Query("""
        SELECT new com.g9team10.backend.dto.ContentCountDTO(
            c.category,
            COUNT(c)
        )
        FROM Content c
        GROUP BY c.category
        ORDER BY c.category
    """)
    List<ContentCountDTO> countByCategory();

}