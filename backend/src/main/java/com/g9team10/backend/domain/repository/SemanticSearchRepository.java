package com.g9team10.backend.domain.repository;

import com.g9team10.backend.domain.model.valueObject.SimilarContent;

import java.util.List;

public interface SemanticSearchRepository {

    List<SimilarContent> searchSimilarContent(
            String query,
            int limit
    );

    List<SimilarContent> searchRecommendations(
            Long id,
            Integer limit
    );
}
