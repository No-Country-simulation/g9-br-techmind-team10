package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentResponseDTO;
import com.g9team10.backend.domain.model.valueObject.CategoryCount;
import com.g9team10.backend.domain.service.ContentCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/count")
@RequiredArgsConstructor
@Tag(name = "Contador de Conteúdo", description = "Endpoint responsável pela contagem de conteúdos")
public class ContentCountController {

    private final ContentCountService service;

    @GetMapping
    @Operation(
            summary = "Contagem dos conteúdos",
            description = "Responsável pela contagem de conteúdos dentro de uma categoria específica."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contagem feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Nenhum conteúdo cadastrado"
    )
    public ResponseEntity<List<CategoryCount>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
