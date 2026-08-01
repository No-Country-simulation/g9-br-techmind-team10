package com.g9team10.backend.service;

import com.g9team10.backend.domain.service.ContentCountService;
import com.g9team10.backend.domain.model.valueObject.CategoryCount;
import com.g9team10.backend.domain.repository.ContentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - ContentCountService")
class ContentServiceTest {

    @Mock
    private ContentRepository repository;

    @InjectMocks
    private ContentCountService contentCountService;

    @Test
    @DisplayName("Deve chamar repositório para contagem por categoria")
    void deveBuscarContagemPorCategoria() {
        // Arrange
        List<CategoryCount> listaMock = List.of();
        when(repository.countByCategory()).thenReturn(listaMock);

        // Act
        var resultado = contentCountService.findAll();

        // Assert
        assertNotNull(resultado, "A lista de contagem não deve ser nula");
        verify(repository).countByCategory();
    }
}