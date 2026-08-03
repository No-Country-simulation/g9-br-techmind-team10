package com.g9team10.backend.api.controller;

import com.g9team10.backend.api.dto.response.ContentResponseDTO;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
@Tag(name = "Favoritos", description = "Endpoint responsável pela marcação e desmarcação de conteúdos favoritos")
public class ContentFavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{id}/favorite")
    @Operation(
            summary = "Favorita conteúdo",
            description = "Responsável por marcar conteúdos como favoritos."

    )
    @ApiResponse(
            responseCode = "204",
            description = "Marcação feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Nenhum conteúdo cadastrado para marcar!"
    )
    public ResponseEntity<Void> checkFavorite(@PathVariable Long id, @AuthenticationPrincipal User user) {
        favoriteService.check(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/favorite")
    @Operation(
            summary = "Desfavoritar conteúdo",
            description = "Responsável por desmarcar conteúdos como favoritos."

    )
    @ApiResponse(
            responseCode = "204",
            description = "Desmarcação feita com sucesso!"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Nenhum conteúdo cadastrado para desmarcar!"
    )
    public ResponseEntity<Void> uncheckFavorite(@PathVariable Long id, @AuthenticationPrincipal User user) {
        favoriteService.uncheck(user, id);
        return ResponseEntity.noContent().build();
    }
}
