package com.g9team10.backend.infra.vectorsearch;

import com.g9team10.backend.domain.repository.EmbeddingGateway;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OracleEmbeddingGateway implements EmbeddingGateway {

    private final EntityManager entityManager;

    @Override
    public void generateEmbeddingForContent(Long contentId) {
        entityManager.createNativeQuery("""
                        UPDATE content_chunk
                        SET embedding = VECTOR_EMBEDDING(MULTILINGUAL_EMBED USING CONCAT('passage: ', text) AS DATA)
                        WHERE content_id = :contentId
                            AND embedding IS NULL
                        """)
                .setParameter("contentId", contentId)
                .executeUpdate();
    }

    @Override
    public void generateCentroid(Long contentId) {
        entityManager.createNativeQuery("""
                UPDATE content c
                SET embedding_centroid = (
                    SELECT AVG(cc.embedding)
                    FROM content_chunk cc
                    WHERE cc.content_id = c.id
                )
                WHERE c.id = :id
                """)
                .setParameter("id", contentId)
                .executeUpdate();
    }
}
