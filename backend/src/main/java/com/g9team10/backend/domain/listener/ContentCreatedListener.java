package com.g9team10.backend.domain.listener;

import com.g9team10.backend.domain.event.ContentCreatedEvent;
import com.g9team10.backend.domain.service.EmbeddingProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ContentCreatedListener {

    private final EmbeddingProcessingService embeddingProcessingService;

    @Async("embeddingExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onContentCreated(ContentCreatedEvent event) {
        embeddingProcessingService.process(event.contentId());
    }
}
