package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentDetailDTO;
import com.g9team10.backend.core.config.TrustPropertiesConfig;
import com.g9team10.backend.domain.model.Level;
import com.g9team10.backend.domain.model.valueObject.SimilarContent;
import com.g9team10.backend.domain.service.ContentSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
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
    @GetMapping("/search")
    public ResponseEntity<List<ContentDetailDTO>> search(@RequestParam List<String> tags,
                                                         @RequestParam(required = false) String level) {
        return ResponseEntity.ok(contentSearchService.searchByTags(tags, Level.fromNullable(level)).stream()
                .map(content -> ContentDetailDTO.fromEntity(content, trustProperties))
                .toList());
    }

    @GetMapping("/search-similar")
    public ResponseEntity<List<SimilarContent>> search(@RequestParam String q, @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(contentSearchService.searchSimilar(q, limit));
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<SimilarContent>> recommendations(@PathVariable Long id, @RequestParam(defaultValue = "6") Integer limit) {
        return ResponseEntity.ok(contentSearchService.searchRecommendations(id, limit));
    }
}