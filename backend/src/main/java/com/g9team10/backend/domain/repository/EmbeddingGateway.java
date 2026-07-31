package com.g9team10.backend.domain.repository;

public interface EmbeddingGateway {
    void generateEmbeddingForContent(Long contentId);

    void generateCentroid(Long contentId);
}
