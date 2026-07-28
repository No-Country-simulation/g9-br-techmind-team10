package com.g9team10.backend.domain.repository;

import com.g9team10.backend.api.dto.response.SimilarContentDTO;

import java.util.List;

public interface SemanticSearchRepository {

    List<SimilarContentDTO> searchSimilarContent(
            String query,
            int limit
    );

    List<SimilarContentDTO> searchRecommendations(
            Long id,
            Integer limit
    );
}
