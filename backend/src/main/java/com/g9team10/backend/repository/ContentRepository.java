package com.g9team10.backend.repository;

import com.g9team10.backend.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
}