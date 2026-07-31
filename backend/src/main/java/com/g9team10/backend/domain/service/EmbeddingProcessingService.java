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

import java.util.ArrayList;
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
            List<ContentChunk> contentChunks = new ArrayList<>();
            for (int i = 0; i < chunks.size(); i++) {
                contentChunks.add(new ContentChunk(content, i, chunks.get(i)));
            }
            contentChunkRepository.saveAll(contentChunks);

            embeddingGateway.generateEmbeddingForContent(contentId);
            embeddingGateway.generateCentroid(content.getId());

            content.completeEmbedding();
        } catch (Exception e) {
            content.failEmbedding();
            throw e;
        }
    }
}
