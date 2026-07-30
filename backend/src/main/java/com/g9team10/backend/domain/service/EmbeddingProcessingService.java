package com.g9team10.backend.domain.service;

import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.ContentChunk;
import com.g9team10.backend.domain.repository.ContentChunkRepository;
import com.g9team10.backend.domain.repository.ContentRepository;
import com.g9team10.backend.domain.repository.EmbeddingGateway;
import com.g9team10.backend.shared.TextChunker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingProcessingService {

    private final ContentChunkRepository contentChunkRepository;
    private final EmbeddingGateway embeddingGateway;
    private final ContentRepository contentRepository;

    @Transactional
    public void process(Long contentId) {
        Content content = contentRepository.findRequired(contentId);
        content.startEmbedding();

        try {
            List<String> chunks = TextChunker.split(content.getText());
            for (int i = 0; i < chunks.size(); i++) {
                ContentChunk chunk = contentChunkRepository.save(new ContentChunk(content, i, chunks.get(i)));
                embeddingGateway.generateEmbedding(chunk.getId(), chunk.getText());
            }

            embeddingGateway.generateCentroid(content.getId());

            content.completeEmbedding();
        } catch (Exception e) {
            content.failEmbedding();
            throw e;
        }
    }
}
