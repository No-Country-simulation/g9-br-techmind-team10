package com.g9team10.backend.repository;

import com.g9team10.backend.model.ContentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentChunkRepository extends JpaRepository<ContentChunk, Long> {

    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE content_chunk
            SET embedding = VECTOR_EMBEDDING(MULTILINGUAL_EMBED USING CONCAT('passage: ', :text) AS DATA)
            WHERE id = :id
            """)
    void generateEmbedding(Long id, String text);
}
