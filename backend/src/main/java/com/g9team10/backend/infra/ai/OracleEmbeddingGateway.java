package com.g9team10.backend.infra.ai;

import com.g9team10.backend.domain.repository.EmbeddingGateway;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OracleEmbeddingGateway implements EmbeddingGateway {

    private final EntityManager entityManager;

    @Override
    public void generateEmbedding(Long chunkId, String text) {
        entityManager.createNativeQuery("""
                        UPDATE content_chunk
                        SET embedding = VECTOR_EMBEDDING(MULTILINGUAL_EMBED USING CONCAT('passage: ', :text) AS DATA)
                        WHERE id = :id
                        """)
                .setParameter("id", chunkId)
                .setParameter("text", text)
                .executeUpdate();
    }

    @Override
    public void generateCentroid(Long chunkId) {
        entityManager.createNativeQuery("""
                UPDATE content c
                SET embedding_centroid = (
                    SELECT AVG(cc.embedding)
                    FROM content_chunk cc
                    WHERE cc.content_id = c.id
                )
                WHERE c.id = :id
                """)
                .setParameter("id", chunkId)
                .executeUpdate();
    }
}
