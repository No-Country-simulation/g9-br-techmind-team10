package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.TagGroupResponseDTO;
import com.g9team10.backend.domain.service.TagCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@Tag(name = "Tags", description = "Endpoints para consulta das tags aceitas pelo sistema")
public class TagController {

    private final TagCatalogService tagCatalogService;

    @Operation(
            summary = "Listar tags aceitas",
            description = "Retorna todas as tags disponíveis para classificação de conteúdos."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de tags retornada com sucesso"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Erro ao retornar as listas"
    )
    @GetMapping("/tags")
    public List<TagGroupResponseDTO> getAcceptedTags(){
        return tagCatalogService.getAcceptedTags();
    }
}
