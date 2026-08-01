package com.g9team10.backend.service;

import com.g9team10.backend.domain.model.valueObject.CategoryCount;
import com.g9team10.backend.domain.service.ContentCountService;
import com.g9team10.backend.domain.repository.ContentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Deve retornar lista de contagens por categoria")
    void deveRetornarContagens() {
        List<CategoryCount> listaMock = List.of(
            new CategoryCount("Educativo", 5L),
            new CategoryCount("Entretenimento", 3L)
        );

        when(repository.countByCategory()).thenReturn(listaMock);

        List<CategoryCount> resultado = contentCountService.findAll();

        assertAll(
            () -> assertNotNull(resultado, "Lista não pode ser nula"),
            () -> assertFalse(resultado.isEmpty(), "Deveria retornar itens"),
            () -> assertEquals(2, resultado.size(), "Quantidade deve bater com o mock"),
            () -> verify(repository).countByCategory()
        );
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver conteúdos")
    void deveRetornarVazioSemConteudos() {
        when(repository.countByCategory()).thenReturn(List.of());

        List<CategoryCount> resultado = contentCountService.findAll();

        assertAll(
            () -> assertNotNull(resultado),
            () -> assertTrue(resultado.isEmpty())
        );
    }
}