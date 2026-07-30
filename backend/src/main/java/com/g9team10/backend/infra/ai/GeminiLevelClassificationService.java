package com.g9team10.backend.infra.ai;

import com.g9team10.backend.domain.model.Level;
import com.g9team10.backend.domain.service.LevelClassificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.text.Normalizer;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class GeminiLevelClassificationService implements LevelClassificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeminiLevelClassificationService.class);

    private final WebClient geminiWebClient;
    private final String apiKey;
    private final String model;

    public GeminiLevelClassificationService(
            @Qualifier("geminiWebClient") WebClient geminiWebClient,
            @Value("${gemini.api.key:}") String apiKey,
            @Value("${gemini.api.model:gemini-2.5-flash-lite}") String model
    ) {
        this.geminiWebClient = geminiWebClient;
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public Level classify(String title, String text) {
        if (apiKey == null || apiKey.isBlank()) {
            return classifyByKeyword(title, text);
        }

        try {
            return askGemini(title, text);
        } catch (Exception exception) {
            LOGGER.warn("Gemini level classification failed; using the local fallback.", exception);
        }

        return classifyByKeyword(title, text);
    }

    private Level askGemini(String title, String text) {
        String prompt = """
                Classify the difficulty level of the technical content below.
                Reply with exactly one of these values:
                
                BASIC
                INTERMEDIATE
                ADVANCED
                
                Do not include any other words.
                Do not use punctuation.
                Do not explain your answer.
                
                Title: %s
                Text: %s
                """.formatted(safeText(title), truncate(safeText(text), 2000));

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
                "generationConfig", Map.of("temperature", 0, "maxOutputTokens", 10)
        );

        Map<?, ?> response = geminiWebClient.post()
                .uri("/v1beta/models/{model}:generateContent", model)
                .header("x-goog-api-key", apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(8))
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(1)))
                .block();

        return Level.from(extractResponseText(response));
    }

    private String extractResponseText(Map<?, ?> response) {
        if (response == null) {
            return null;
        }

        Object candidatesValue = response.get("candidates");
        if (!(candidatesValue instanceof List<?> candidates) || candidates.isEmpty()
                || !(candidates.get(0) instanceof Map<?, ?> candidate)
                || !(candidate.get("content") instanceof Map<?, ?> content)
                || !(content.get("parts") instanceof List<?> parts) || parts.isEmpty()
                || !(parts.get(0) instanceof Map<?, ?> part)) {
            return null;
        }

        Object text = part.get("text");
        return text == null ? null : text.toString();
    }

    private Level classifyByKeyword(String title, String text) {
        String content = normalizeText(safeText(title) + " " + safeText(text));
        if (containsAny(content, "iniciante", "basico", "introducao", "primeiros passos", "do zero", "fundamentos")) {
            return Level.BASIC;
        }
        if (containsAny(content, "avancado", "arquitetura", "performance", "otimizacao", "escalabilidade", "deep dive")) {
            return Level.ADVANCED;
        }
        return Level.INTERMEDIATE;
    }

    private boolean containsAny(String text, String... values) {
        for (String value : values) {
            if (text.contains(value)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeText(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }

    private String truncate(String value, int maxLength) {
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}