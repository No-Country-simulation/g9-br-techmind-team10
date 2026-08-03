package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.request.ContentRequestDTO;
import com.g9team10.backend.api.dto.request.CorrectionTagsRequestDTO;
import com.g9team10.backend.api.dto.response.ContentDetailDTO;
import com.g9team10.backend.api.dto.response.ContentResponseDTO;
import com.g9team10.backend.core.config.TrustPropertiesConfig;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.service.ContentCreateService;
import com.g9team10.backend.domain.service.ContentReviewService;
import com.g9team10.backend.domain.service.ContentViewService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
@Tag(name = "Análise de Conteúdo", description = "Endpoint responsável pela classificação de conteúdos")
public class ContentController {

    private final ContentCreateService contentCreateService;
    private final TrustPropertiesConfig trustProperties;
    private final ContentReviewService contentReviewService;
    private final ContentViewService contentViewService;

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
        var content = contentViewService.view(user, id);
        return ResponseEntity.ok(ContentDetailDTO.fromEntity(content, trustProperties));
    }

    @PutMapping("/{id}/tags")
    @Operation(
            summary = "Corrigi as Tags",
            description = "Permitir que um usuário autenticado corrija as tags associadas a um conteúdo quando a classificação automática do sistema não estiver adequada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tags corrigidas!"

    )
    @ApiResponse(
            responseCode = "404",
            description = "Tags não foram corrigidas!"
    )
    public ResponseEntity<ContentDetailDTO> fixTags(
            @PathVariable Long id,
            @Valid @RequestBody CorrectionTagsRequestDTO request
    ) {
        var content = contentReviewService.fixTags(id, request.tags());
        return ResponseEntity.ok(ContentDetailDTO.fromEntity(content, trustProperties));
    }

    @PostMapping("/{id}/tags/confirm")
    @Operation(
            summary = "Confirma as Tags",
            description = "Permitir que o usuário confirme que as tags sugeridas automaticamente pelo sistema estão corretas, sem precisar enviar uma nova lista de tags."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tags confirmadas"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Tags não confirmadas"
    )
    public ResponseEntity<ContentDetailDTO> confirmTags(@PathVariable Long id) {
        var content = contentReviewService.confirmTags(id);
        return ResponseEntity.ok(ContentDetailDTO.fromEntity(content, trustProperties));
    }
}