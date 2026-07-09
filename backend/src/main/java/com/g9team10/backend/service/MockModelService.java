package com.g9team10.backend.service;

import com.g9team10.backend.dto.ModelPredictRequestDTO;
import com.g9team10.backend.dto.ModelPredictResponseDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class MockModelService implements ModelPredictionService {

    @Override
    public ModelPredictResponseDTO predict(ModelPredictRequestDTO request) {
        String combinedText = (request.title() + " " + request.text()).toLowerCase();

        if (containsAny(combinedText, "react", "html", "css", "javascript", "frontend")) {
            return new ModelPredictResponseDTO(
                    "Frontend",
                    0.91,
                    List.of("HTML", "CSS", "JavaScript")
            );
        }

        if (containsAny(combinedText, "python", "pandas", "scikit", "tf-idf", "machine learning", "modelo")) {
            return new ModelPredictResponseDTO(
                    "Data Science",
                    0.88,
                    List.of("Python", "TF-IDF", "Machine Learning")
            );
        }

        if (containsAny(combinedText, "oci", "cloud", "docker", "compute", "bucket", "object storage")) {
            return new ModelPredictResponseDTO(
                    "Cloud",
                    0.86,
                    List.of("OCI", "Docker", "Object Storage")
            );
        }

        if (containsAny(combinedText, "sql", "database", "banco", "jpa", "h2")) {
            return new ModelPredictResponseDTO(
                    "Database",
                    0.84,
                    List.of("SQL", "Database", "JPA")
            );
        }

        if (containsAny(combinedText, "security", "token", "jwt", "senha", "autenticação", "login")) {
            return new ModelPredictResponseDTO(
                    "Security",
                    0.82,
                    List.of("Authentication", "Token", "Security")
            );
        }

        return new ModelPredictResponseDTO(
                "Backend",
                0.89,
                List.of("Java", "Spring Boot", "API REST")
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