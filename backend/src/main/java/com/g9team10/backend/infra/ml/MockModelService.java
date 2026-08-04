package com.g9team10.backend.infra.ml;

import com.g9team10.backend.domain.model.valueObject.ModelPredictRequest;
import com.g9team10.backend.domain.model.valueObject.ModelPredictResult;
import com.g9team10.backend.domain.service.ModelPredictionService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@ConditionalOnProperty(
        name = "model.prediction.mode",
        havingValue = "mock",
        matchIfMissing = true
)
@Service
public class MockModelService implements ModelPredictionService {

    @Override
    public ModelPredictResult predict(ModelPredictRequest request) {
        String combinedText = (request.title() + " " + request.text()).toLowerCase();

        if (containsAny(combinedText, "react", "html", "css", "javascript", "frontend")) {
            return new ModelPredictResult(
                    "frontend",
                    0.91,
                    List.of("frontend", "html", "css", "javascript")
            );
        }

        if (containsAny(combinedText, "python", "pandas", "scikit", "tf-idf", "machine learning", "modelo")) {
            return new ModelPredictResult(
                    "data-science",
                    0.88,
                    List.of("data-science", "python", "tf-idf", "machine-learning")
            );
        }

        if (containsAny(combinedText, "oci", "cloud", "docker", "compute", "bucket", "object storage")) {
            return new ModelPredictResult(
                    "cloud",
                    0.86,
                    List.of("cloud", "oci", "docker", "object-storage")
            );
        }

        if (containsAny(combinedText, "sql", "database", "banco", "jpa", "h2", "repository")) {
            return new ModelPredictResult(
                    "database",
                    0.84,
                    List.of("database", "sql", "jpa", "repository")
            );
        }

        if (containsAny(combinedText, "security", "token", "jwt", "senha", "autenticação", "login")) {
            return new ModelPredictResult(
                    "security",
                    0.82,
                    List.of("security", "authentication", "jwt", "token", "spring-security")
            );
        }

        return new ModelPredictResult(
                "backend",
                0.89,
                List.of("backend", "java", "spring-boot", "api-rest")
        );
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}