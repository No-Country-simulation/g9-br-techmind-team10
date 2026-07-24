package com.g9team10.backend.repository;

import com.g9team10.backend.dto.SimilarContentDTO;

import java.util.List;

public interface SemanticSearchRepository {

    List<SimilarContentDTO> searchSimilarContent(
            String query,
            int limit
    );

}
