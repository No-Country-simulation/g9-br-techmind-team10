package com.g9team10.backend.domain.repository;

public interface EmbeddingGateway {
    void generateEmbedding(Long chunkId, String text);

    void generateCentroid(Long chunkId);
}
