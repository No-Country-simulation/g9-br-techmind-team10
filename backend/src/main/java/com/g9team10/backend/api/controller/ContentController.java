package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.request.ContentRequestDTO;
import com.g9team10.backend.api.dto.request.CorrectionTagsRequestDTO;
import com.g9team10.backend.api.dto.response.ContentDetailDTO;
import com.g9team10.backend.api.dto.response.ContentResponseDTO;
import com.g9team10.backend.core.config.TrustPropertiesConfig;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.model.valueObject.SimilarContent;
import com.g9team10.backend.domain.repository.ContentRepository;
import com.g9team10.backend.domain.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
@Tag(name = "Análise de Conteúdo", description = "Endpoint responsável pela classificação de conteúdos")
public class ContentController {

    private final ContentCreateService contentCreateService;
    private final HistoryService historyService;
    private final TrustPropertiesConfig trustProperties;
    private final ContentReviewService contentReviewService;
    private final ContentSearchService contentSearchService;
    private final ContentRepository contentRepository;

    @Operation(
            summary = "Analisar conteúdo",
            description = "Recebe um conteúdo, realiza a análise e retorna o resultado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Conteúdo analisado com sucesso",
            content = @Content(schema = @Schema(implementation = ContentResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos enviados na requisição"
    )
    @PostMapping
    public ResponseEntity<ContentResponseDTO> analysis(@RequestBody @Valid ContentRequestDTO request) {
        return ResponseEntity.ok(ContentResponseDTO.fromEntity(contentCreateService.create(request.title(), request.text())));
    }

    @Operation(
            summary = "Acessar conteúdo",
            description = "Acessa conteúdo analisado e salvo no banco de dados."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Conteúdo encontrado",
            content = @Content(schema = @Schema(implementation = ContentResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Conteúdo não encontrado"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ContentDetailDTO> getContent(@PathVariable Long id, @AuthenticationPrincipal User user) {
        var content = contentRepository.findRequired(id);
        historyService.registerView(user, id);
        return ResponseEntity.ok(ContentDetailDTO.fromEntity(content, trustProperties));
    }

    @GetMapping("/search-similar")
    public ResponseEntity<List<SimilarContent>> search(@RequestParam String q, @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(contentSearchService.searchSimilar(q, limit));
    }

    @GetMapping("/{id}/recommendations")
    public ResponseEntity<List<SimilarContent>> recommendations(@PathVariable Long id, @RequestParam(defaultValue = "6") Integer limit) {
        return ResponseEntity.ok(contentSearchService.searchRecommendations(id, limit));
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<ContentDetailDTO> fixTags(
            @PathVariable Long id,
            @Valid @RequestBody CorrectionTagsRequestDTO request
    ) {
        var content = contentReviewService.fixTags(id, request.tags());
        return ResponseEntity.ok(ContentDetailDTO.fromEntity(content, trustProperties));
    }

    @PatchMapping("/{id}/tags/confirm")
    public ResponseEntity<ContentDetailDTO> confirmTags(@PathVariable Long id) {
        var content = contentReviewService.confirmTags(id);
        return ResponseEntity.ok(ContentDetailDTO.fromEntity(content, trustProperties));
    }
}