package com.g9team10.backend.service;

import com.g9team10.backend.dto.ContentRequestDTO;
import com.g9team10.backend.dto.ContentResponseDTO;
import com.g9team10.backend.dto.ModelPredictRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final ModelPredictionService modelPredictionService;

    public ContentResponseDTO analysis(ContentRequestDTO request) {
        var predictRequest = new ModelPredictRequestDTO(request.title(), request.text());
        var response = modelPredictionService.predict(predictRequest);

        return new ContentResponseDTO(response.category(),
                response.probability(),
                response.additionalInformation());
    }
}
