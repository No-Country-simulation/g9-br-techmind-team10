package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentDetailDTO;
import com.g9team10.backend.core.config.TrustPropertiesConfig;
import com.g9team10.backend.domain.model.Level;
import com.g9team10.backend.domain.service.ContentSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Busca de Conteúdo", description = "Endpoint responsável pela busca de conteúdos")
public class ContentSearchController {

    private final ContentSearchService contentSearchService;
    private final TrustPropertiesConfig trustProperties;

    @Operation(
            summary = "Busca conteúdo",
            description = "Busca um conteúdo, já classificado, por meio das tags "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Conteúdo foi achado com sucesso!"

    )
    @ApiResponse(
            responseCode = "400",
            description = "Erro ao buscar o conteúdo"

    )
    @GetMapping("/content/search")
    public ResponseEntity<List<ContentDetailDTO>> search(@RequestParam List<String> tags,
                                                         @RequestParam(required = false) String level) {
        return ResponseEntity.ok(contentSearchService.searchByTags(tags, Level.from(level)).stream()
                .map(content -> ContentDetailDTO.fromEntity(content, trustProperties))
                .toList());
    }
}