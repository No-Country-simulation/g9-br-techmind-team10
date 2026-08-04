package com.g9team10.backend.infra.ml;

import com.g9team10.backend.domain.exception.ModelPredictUnavailableException;
import com.g9team10.backend.domain.model.valueObject.ModelPredictRequest;
import com.g9team10.backend.domain.model.valueObject.ModelPredictResult;
import com.g9team10.backend.domain.service.ModelPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@ConditionalOnProperty(
        name = "model.prediction.mode",
        havingValue = "service"
)

@Service
@RequiredArgsConstructor
public class ModelClientService implements ModelPredictionService {

    private final WebClient webClient;

    @Override
    public ModelPredictResult predict(ModelPredictRequest request) {
        try {
            return webClient.post()
                    .uri("/predict")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ModelPredictResult.class)
                    .retryWhen(Retry.backoff(1, Duration.ofSeconds(2)))
                    .block();
        } catch (Exception e) {
            throw new ModelPredictUnavailableException(e);
        }
    }
}
