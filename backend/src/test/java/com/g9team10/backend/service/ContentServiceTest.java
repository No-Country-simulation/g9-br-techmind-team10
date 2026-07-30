package com.g9team10.backend.service;

import java.util.List;
import com.g9team10.backend.dto.ContentRequestDTO;
import com.g9team10.backend.dto.ContentResponseDTO;
import com.g9team10.backend.dto.ModelPredictRequestDTO;
import com.g9team10.backend.dto.ModelPredictResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @Mock
    private ModelPredictionService modelPredictionService;

    @InjectMocks
    private ContentService contentService;

    @Test
    void deveAnalisarConteudoComSucesso() {
      // Arrange
        ContentRequestDTO request = new ContentRequestDTO("Titulo Teste", "Texto de teste para analise");
    ModelPredictResponseDTO mockResponse = new ModelPredictResponseDTO("Tecnologia", 0.95, List.of("Info extra"));
        when(modelPredictionService.predict(any(ModelPredictRequestDTO.class))).thenReturn(mockResponse);

        // Act
        ContentResponseDTO response = contentService.analysis(request);

        // Assert
        assertNotNull(response);
    }
}