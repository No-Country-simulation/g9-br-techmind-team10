package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.request.UserContentTagRequestDTO;
import com.g9team10.backend.api.dto.response.ContentSummaryDTO;
import com.g9team10.backend.api.dto.response.UserContentTagResponseDTO;
import com.g9team10.backend.domain.model.Content;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.model.UserContentTag;
import com.g9team10.backend.domain.service.UserContentTagService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Tags pessoais", description = "Endpoints responsáveis pelas tags personalizadas do usuário")
public class UserContentTagController {

    private final UserContentTagService userContentTagService;

    @GetMapping("/{contentId}/personal-tags")
    @Operation(
            summary = "Consulta Tags personalizadas",
            description =  "Permitir que o usuário consulte todas as tags personalizadas que ele adicionou a um conteúdo específico."

    )
    @ApiResponse(
            responseCode = "204",
            description = "A consulta da Tag foi feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A consulta da Tag falhou!"
    )
    public ResponseEntity<List<UserContentTagResponseDTO>> listByContent(
            @PathVariable Long contentId,
            @AuthenticationPrincipal User user
    ) {
        List<UserContentTagResponseDTO> response = userContentTagService
                .listByContent(user, contentId)
                .stream()
                .map(UserContentTagResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{contentId}/personal-tags")
    @Operation(
            summary = "Criar Tags personalizadas",
            description = "Permitir que um usuário autenticado crie tags próprias para organizar conteúdos da biblioteca de forma personalizada."

    )
    @ApiResponse(
            responseCode = "204",
            description = "A criação da Tag foi feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A criação da Tag falhou!"
    )
    public ResponseEntity<UserContentTagResponseDTO> create(
            @PathVariable Long contentId,
            @RequestBody @Valid UserContentTagRequestDTO request,
            @AuthenticationPrincipal User user
    ) {
        UserContentTag tag = userContentTagService.create(user, contentId, request.name());

        return ResponseEntity.ok(UserContentTagResponseDTO.fromEntity(tag));
    }

    @DeleteMapping("/{contentId}/personal-tags/{tagId}")
    @Operation(
            summary = "Permitir que o usuário remova uma tag personalizada previamente criada para um conteúdo."

    )
    @ApiResponse(
            responseCode = "204",
            description = "A exclusão da Tag foi feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A exclusão da Tag falhou!"
    )
    public ResponseEntity<Void> delete(
            @PathVariable Long contentId,
            @PathVariable Long tagId,
            @AuthenticationPrincipal User user
    ) {
        userContentTagService.delete(user, contentId, tagId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/personal-tags/search")
    @Operation(
            summary = "Permitir que o usuário encontre conteúdos da biblioteca a partir das tags personalizadas criadas por ele."

    )
    @ApiResponse(
            responseCode = "204",
            description = "A consulta da Tag foi feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "A consulta da Tag falhou!"
    )
    public ResponseEntity<List<ContentSummaryDTO>> searchContentsByPersonalTags(
            @RequestParam("tags") List<String> tags,
            @AuthenticationPrincipal User user
    ) {
        List<Content> contents = userContentTagService.searchContentsByPersonalTags(user, tags);

        List<ContentSummaryDTO> response = contents.stream()
                .map(ContentSummaryDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}