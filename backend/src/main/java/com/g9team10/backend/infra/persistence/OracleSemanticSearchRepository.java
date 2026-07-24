package com.g9team10.backend.infra.persistence;

import com.g9team10.backend.dto.SimilarContentDTO;
import com.g9team10.backend.repository.SemanticSearchRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OracleSemanticSearchRepository implements SemanticSearchRepository {

    private final EntityManager entityManager;

    @Override
    public List<SimilarContentDTO> searchSimilarContent(String query, int limit) {
        String sql = """
                WITH ranked_posts AS (
                    SELECT
                        cc.content_id,
                        MIN(
                            VECTOR_DISTANCE(
                                cc.embedding,
                                VECTOR_EMBEDDING(
                                    MULTILINGUAL_EMBED
                                    USING CONCAT('query: ', :query) AS DATA
                                ),
                                COSINE
                            )
                        ) AS distance
                    FROM content_chunk cc
                    WHERE cc.embedding IS NOT NULL
                    GROUP BY cc.content_id
                    ORDER BY distance
                FETCH FIRST :limit ROWS ONLY
                )
                SELECT
                    c.id,
                    c.title,
                    c.text AS content,
                    c.category,
                    rp.distance
                FROM ranked_posts rp
                JOIN content c
                    ON c.id = rp.content_id
                ORDER BY rp.distance
                """;

        List<Object[]> result = entityManager
                .createNativeQuery(sql)
                .setParameter("query", query)
                .setParameter("limit", limit)
                .getResultList();

        return result.stream()
                .map(row -> {
                    Clob clob = (Clob) row[2];
                    try {
                        return new SimilarContentDTO(
                                ((Number) row[0]).longValue(),
                                (String) row[1],
                                clob.getSubString(1, (int) clob.length()),
                                (String) row[3],
                                ((Number) row[4]).doubleValue()
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }
}
